package com.practicum.pizzahouse.service;

import com.practicum.pizzahouse.api.dto.OrderDetails;
import com.practicum.pizzahouse.api.dto.OrderSummary;
import com.practicum.pizzahouse.model.Ingredient;
import com.practicum.pizzahouse.model.IngredientRepository;
import com.practicum.pizzahouse.model.PizzaRepository;
import lombok.SneakyThrows;
import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.executable.ExecutableValidator;
import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@ExtendWith(SpringExtension.class)
public class PizzaServiceImplTest {

  private static Validator validator;
  private static ValidatorFactory validatorFactory;
  public static AutoCloseable openedMocks;

  @Mock
  private PizzaRepository pizzaRepository;

  private AddressService addressService;

  @BeforeAll
  public static void init() {
    validatorFactory = Validation.byProvider(HibernateValidator.class)
        .configure()
        .buildValidatorFactory();
    validator = validatorFactory.getValidator();
    openedMocks = MockitoAnnotations.openMocks(PizzaServiceImplTest.class);
  }

  @AfterAll
  @SneakyThrows
  public static void cleanup() {
    openedMocks.close();
    validatorFactory.close();
  }

  @BeforeEach
  public void prepareMocks() {
    addressService = Mockito.mock(AddressService.class);
  }

  @Test
  @SneakyThrows
  public void testMakeOrder_SupplyNull_ConstraintViolation() {
    IngredientRepository ingredientRepository = Mockito.mock(IngredientRepository.class);
    PizzaRepository pizzaRepository = Mockito.mock(PizzaRepository.class);

    PizzaService pizzaService = new PizzaServiceImpl(addressService, ingredientRepository, pizzaRepository);

    ExecutableValidator executableValidator = validator.forExecutables();
    Method method = pizzaService.getClass().getMethod("makeOrder", OrderDetails.class);
    Set<ConstraintViolation<PizzaService>> violations =
        executableValidator.validateParameters(pizzaService, method, new Object[] { null });

    Assertions.assertEquals(1, violations.size());
    violations.stream()
        .map(ConstraintViolation::getConstraintDescriptor)
        .map(ConstraintDescriptor::getAnnotation)
        .map(Annotation::annotationType)
        .forEach(annotationType -> Assertions.assertEquals(NotNull.class, annotationType));
  }

  @Test
  public void testMakeOrder_SupplyCorrectOrder_ReturnsCorrectSummary() {
    LocalDateTime date = LocalDateTime.now().plusHours(1L);
    List<Ingredient> ingredients = List.of(
        new Ingredient("Сыр Пармезан", BigDecimal.valueOf(10)),
        new Ingredient("Сыр Дор Блю", BigDecimal.valueOf(15)),
        new Ingredient("Ветчина", BigDecimal.valueOf(10))
    );

    IngredientRepository ingredientRepository = Mockito.mock(IngredientRepository.class);
    Mockito.when(ingredientRepository.findAllByNameIn(List.of("Сыр Пармезан", "Сыр Дор Блю", "Ветчина")))
        .thenReturn(ingredients);
    Mockito.when(pizzaRepository.save(Mockito.any()))
        .thenAnswer(invocation -> invocation.getArgument(0));

    List<Ingredient> ingredientList =
        ingredientRepository.findAllByNameIn(List.of("Сыр Пармезан", "Сыр Дор Блю", "Ветчина"));

    Assertions.assertEquals(ingredients, ingredientList);

    OrderDetails orderDetails = OrderDetails.builder()
        .address("г. Казань, ул. Кремлевская 18")
        .customerName("Алексей")
        .thickness(3)
        .ingredient("Сыр Пармезан")
        .ingredient("Сыр Дор Блю")
        .ingredient("Ветчина")
        .build();

    OrderSummary expectedOrderSummary = new OrderSummary(BigDecimal.valueOf(35), date, "Алексей");

    AddressService addressService = Mockito.mock(AddressService.class);
    Mockito.when(addressService.estimateDelivery(Mockito.anyString()))
        .thenReturn(date);

    PizzaService pizzaService = new PizzaServiceImpl(addressService, ingredientRepository, pizzaRepository);

    OrderSummary actualOrderSummary = pizzaService.makeOrder(orderDetails);

    Assertions.assertEquals(expectedOrderSummary, actualOrderSummary);
  }
}
