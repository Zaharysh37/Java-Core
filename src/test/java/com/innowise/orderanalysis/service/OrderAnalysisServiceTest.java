package com.innowise.orderanalysis.service;

import com.innowise.orderanalysis.entity.Category;
import com.innowise.orderanalysis.entity.Customer;
import com.innowise.orderanalysis.entity.Order;
import com.innowise.orderanalysis.entity.OrderItem;
import com.innowise.orderanalysis.entity.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

class OrderAnalysisServiceTest {

    private List<Order> orders;
    private Customer customer1;
    private Customer customer2;
    private Customer customer3;

    @BeforeEach
    void setUp() {
        // Создаем тестовых клиентов
        customer1 = new Customer("C001", "Иван Иванов", "ivan@mail.com",
            LocalDateTime.now().minusDays(100), 30, "Москва");
        customer2 = new Customer("C002", "Петр Петров", "petr@mail.com",
            LocalDateTime.now().minusDays(50), 25, "Санкт-Петербург");
        customer3 = new Customer("C003", "Мария Сидорова", "maria@mail.com",
            LocalDateTime.now().minusDays(10), 35, "Москва");

        // Создаем тестовые заказы
        Order order1 = new Order("O001", LocalDateTime.now().minusDays(10),
            customer1, Arrays.asList(
            new OrderItem("Ноутбук", 1, 50000.0, Category.ELECTRONICS),
            new OrderItem("Книга", 3, 500.0, Category.BOOKS)
        ), OrderStatus.DELIVERED);

        Order order2 = new Order("O002", LocalDateTime.now().minusDays(8),
            customer2, Arrays.asList(
            new OrderItem("Смартфон", 2, 25000.0, Category.ELECTRONICS),
            new OrderItem("Футболка", 5, 1500.0, Category.CLOTHING)
        ), OrderStatus.DELIVERED);

        Order order3 = new Order("O003", LocalDateTime.now().minusDays(5),
            customer3, Arrays.asList(
            new OrderItem("Конструктор", 2, 3000.0, Category.TOYS),
            new OrderItem("Книга", 3, 500.0, Category.BOOKS)
        ), OrderStatus.PROCESSING);

        Order order4 = new Order("O004", LocalDateTime.now().minusDays(3),
            customer1, Arrays.asList(
            new OrderItem("Смартфон", 1, 25000.0, Category.ELECTRONICS)
        ), OrderStatus.DELIVERED);

        Order order5 = new Order("O005", LocalDateTime.now().minusDays(2),
            customer2, Arrays.asList(
            new OrderItem("Футболка", 5, 1500.0, Category.CLOTHING),
            new OrderItem("Книга", 3, 500.0, Category.BOOKS)
        ), OrderStatus.CANCELLED);

        // Дополнительные заказы для customer1 (чтобы было > 5)
        Order order6 = new Order("O006", LocalDateTime.now().minusDays(1),
            customer1, Arrays.asList(
            new OrderItem("Ноутбук", 1, 50000.0, Category.ELECTRONICS)
        ), OrderStatus.SHIPPED);

        Order order7 = new Order("O007", LocalDateTime.now(), customer1,
            Arrays.asList(new OrderItem("Смартфон", 1, 25000.0, Category.ELECTRONICS)),
            OrderStatus.DELIVERED);

        Order order8 = new Order("O008", LocalDateTime.now(), customer1,
            Arrays.asList(new OrderItem("Книга", 3, 500.0, Category.BOOKS)),
            OrderStatus.DELIVERED);

        Order order9 = new Order("O009", LocalDateTime.now(), customer1,
            Arrays.asList(new OrderItem("Футболка", 5, 1500.0, Category.CLOTHING)),
            OrderStatus.DELIVERED);

        orders = Arrays.asList(order1, order2, order3, order4, order5, order6, order7, order8, order9);
    }

    @Test
    void testGetUniqueCities() {
        List<String> cities = OrderAnalysisService.getUniqueCities(orders);

        assertEquals(2, cities.size());
        assertTrue(cities.contains("Москва"));
        assertTrue(cities.contains("Санкт-Петербург"));
    }

    @Test
    void testGetUniqueCitiesWithEmptyList() {
        List<String> cities = OrderAnalysisService.getUniqueCities(Collections.emptyList());

        assertNotNull(cities);
        assertTrue(cities.isEmpty());
    }

    @Test
    void testGetUniqueCitiesWithNull() {
        List<String> cities = OrderAnalysisService.getUniqueCities(null);

        assertNotNull(cities);
        assertTrue(cities.isEmpty());
    }

    @Test
    void testGetTotalIncomeForDeliveredOrders() {
        Optional<Double> totalIncome = OrderAnalysisService.getTotalIncomeForDeliveredOrders(orders);

        assertTrue(totalIncome.isPresent());
        // Расчет:
        // order1: ноутбук(50000) + книги(1500) = 51500
        // order2: смартфоны(50000) + футболки(7500) = 57500
        // order4: смартфон(25000) = 25000
        // order7: смартфон(25000) = 25000
        // order8: книги(1500) = 1500
        // order9: футболки(7500) = 7500
        // Итого: 51500 + 57500 + 25000 + 25000 + 1500 + 7500 = 168000
        assertEquals(168000.0, totalIncome.get(), 0.001);
    }

    @Test
    void testGetTotalIncomeForDeliveredOrdersWithNoDelivered() {
        List<Order> onlyCancelled = Arrays.asList(
            new Order("O001", LocalDateTime.now(), customer1,
                Arrays.asList(new OrderItem("Товар", 1, 1000.0, Category.ELECTRONICS)),
                OrderStatus.CANCELLED)
        );

        Optional<Double> totalIncome = OrderAnalysisService.getTotalIncomeForDeliveredOrders(onlyCancelled);

        assertTrue(totalIncome.isEmpty());
    }

    @Test
    void testGetMostPopularProductBySales() {
        Optional<String> popularProduct = OrderAnalysisService.getMostPopularProductBySales(orders);

        assertTrue(popularProduct.isPresent());
        // Футболка: order2(5) + order5(5) + order9(5) = 15 штук
        // Книга: order1(3) + order3(3) + order5(3) + order8(3) = 12 штук
        // Смартфон: order2(2) + order4(1) + order7(1) = 4 штуки
        assertEquals("Футболка", popularProduct.get());
    }

    @Test
    void testGetMostPopularProductBySalesWithNoValidOrders() {
        List<Order> onlyCancelled = Arrays.asList(
            new Order("O001", LocalDateTime.now(), customer1,
                Arrays.asList(new OrderItem("Товар", 1, 1000.0, Category.ELECTRONICS)),
                OrderStatus.CANCELLED)
        );

        Optional<String> popularProduct = OrderAnalysisService.getMostPopularProductBySales(onlyCancelled);

        assertTrue(popularProduct.isEmpty());
    }

    @Test
    void testGetAverageCheckForDeliveredOrders() {
        OptionalDouble averageCheck = OrderAnalysisService.getAverageCheckForDeliveredOrders(orders);

        assertTrue(averageCheck.isPresent());
        // Доставленные заказы:
        // order1(51500), order2(57500), order4(25000), order7(25000), order8(1500), order9(7500)
        // Средний чек: (51500 + 57500 + 25000 + 25000 + 1500 + 7500) / 6 = 28000
        assertEquals(28000.0, averageCheck.getAsDouble(), 0.001);
    }

    @Test
    void testGetAverageCheckForDeliveredOrdersWithNoDelivered() {
        List<Order> noDelivered = Arrays.asList(
            new Order("O001", LocalDateTime.now(), customer1,
                Arrays.asList(new OrderItem("Товар", 1, 1000.0, Category.ELECTRONICS)),
                OrderStatus.PROCESSING)
        );

        OptionalDouble averageCheck = OrderAnalysisService.getAverageCheckForDeliveredOrders(noDelivered);

        assertTrue(averageCheck.isEmpty());
    }

    @Test
    void testGetCustomersWithMoreThan5Orders() {
        List<Customer> customers = OrderAnalysisService.getCustomersWithMoreThan5Orders(orders);

        assertEquals(1, customers.size());
        assertEquals("Иван Иванов", customers.get(0).getName());
        assertTrue(customers.contains(customer1));
    }

    @Test
    void testGetCustomersWithMoreThan5OrdersWithFewOrders() {
        List<Order> fewOrders = Arrays.asList(
            new Order("O001", LocalDateTime.now(), customer1,
                Arrays.asList(new OrderItem("Товар", 1, 1000.0, Category.ELECTRONICS)),
                OrderStatus.DELIVERED)
        );

        List<Customer> customers = OrderAnalysisService.getCustomersWithMoreThan5Orders(fewOrders);

        assertNotNull(customers);
        assertTrue(customers.isEmpty());
    }

    @Test
    void testGetCustomersWithMoreThan5OrdersWithNull() {
        List<Customer> customers = OrderAnalysisService.getCustomersWithMoreThan5Orders(null);

        assertNotNull(customers);
        assertTrue(customers.isEmpty());
    }

    @Test
    void testCustomerWithExactly5Orders() {
        // Создаем ровно 5 заказов для customer2
        List<Order> exactly5Orders = Arrays.asList(
            new Order("O001", LocalDateTime.now(), customer2,
                Arrays.asList(new OrderItem("Товар1", 1, 1000.0, Category.ELECTRONICS)),
                OrderStatus.DELIVERED),
            new Order("O002", LocalDateTime.now(), customer2,
                Arrays.asList(new OrderItem("Товар2", 1, 1000.0, Category.ELECTRONICS)),
                OrderStatus.DELIVERED),
            new Order("O003", LocalDateTime.now(), customer2,
                Arrays.asList(new OrderItem("Товар3", 1, 1000.0, Category.ELECTRONICS)),
                OrderStatus.DELIVERED),
            new Order("O004", LocalDateTime.now(), customer2,
                Arrays.asList(new OrderItem("Товар4", 1, 1000.0, Category.ELECTRONICS)),
                OrderStatus.DELIVERED),
            new Order("O005", LocalDateTime.now(), customer2,
                Arrays.asList(new OrderItem("Товар5", 1, 1000.0, Category.ELECTRONICS)),
                OrderStatus.DELIVERED)
        );

        List<Customer> customers = OrderAnalysisService.getCustomersWithMoreThan5Orders(exactly5Orders);

        assertTrue(customers.isEmpty()); // ровно 5 заказов - не попадает в результат
    }
}
