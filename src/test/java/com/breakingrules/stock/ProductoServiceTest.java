//package com.breakingrules.stock;
//
//import com.breakingrules.stock.productos.entity.Producto;
//import com.breakingrules.stock.productos.entity.Talle;
//import com.breakingrules.stock.productos.repository.ProductoRepository;
//import com.breakingrules.stock.productos.service.ProductoService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import java.math.BigDecimal;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//public class ProductoServiceTest {
//
//        @Mock
//        private ProductoRepository repository;
//
//        @InjectMocks
//        private ProductoService service;
//
//        private Producto productoValido;
//
//        @BeforeEach
//        void setUp() {
//            productoValido = new Producto(
//                    null,
//                    "Remera Negra",
//                    "Remera básica",
//                    Talle.M,
//                    "Negro",
//                    new BigDecimal(10),
//                    10
//            );
//        }
//
//    @Test
//    void deberiaFallarSiNombreEsVacio() {
//        productoValido.setNombre("");
//
//        assertThrows(IllegalArgumentException.class,
//                () -> service.guardar(productoValido));
//
//        verify(repository, never()).save(any());
//    }
//
//    @Test
//    void deberiaFallarSiNombreEsNull() {
//        productoValido.setNombre(null);
//
//        assertThrows(IllegalArgumentException.class,
//                () -> service.guardar(productoValido));
//
//        verify(repository, never()).save(any());
//    }
//
//    @Test
//    void deberiaFallarSiCategoriaEsVacia() {
//        productoValido.setCategoria(" ");
//
//        assertThrows(IllegalArgumentException.class,
//                () -> service.guardar(productoValido));
//
//        verify(repository, never()).save(any());
//    }
//
//    @Test
//    void deberiaFallarSiPrecioEsNegativo() {
//        productoValido.setPrecio(new BigDecimal("-1"));
//
//        assertThrows(IllegalArgumentException.class,
//                () -> service.guardar(productoValido));
//
//        verify(repository, never()).save(any());
//    }
//
//    @Test
//    void deberiaFallarSiStockEsNull() {
//        productoValido.setStock(null);
//
//        assertThrows(IllegalArgumentException.class,
//                () -> service.guardar(productoValido));
//
//        verify(repository, never()).save(any());
//    }
//
//    @Test
//    void deberiaGuardarProductoValido() {
//        when(repository.save(any())).thenReturn(productoValido);
//
//        Producto resultado = service.guardar(productoValido);
//
//        assertNotNull(resultado);
//        verify(repository).save(productoValido);
//    }
//
//    @Test
//    void deberiaFallarSiPrecioEsCero() {
//        productoValido.setPrecio(new BigDecimal("0"));
//
//        assertThrows(IllegalArgumentException.class,
//                () -> service.guardar(productoValido));
//
//        verify(repository, never()).save(any());
//    }
//
//    @Test
//    void deberiaFallarSiStockEsNegativo() {
//        productoValido.setStock(-5);
//
//        assertThrows(IllegalArgumentException.class,
//                () -> service.guardar(productoValido));
//
//        verify(repository, never()).save(any());
//    }
//
//    @Test
//    void deberiaFallarSiIdEsNullAlEliminar() {
//        assertThrows(IllegalArgumentException.class,
//                () -> service.eliminar(null));
//
//        verify(repository, never()).deleteById(any());
//    }
//
//    @Test
//    void deberiaFallarAlEliminarProductoInexistente() {
//        when(repository.existsById(1)).thenReturn(false);
//
//        assertThrows(IllegalArgumentException.class,
//                () -> service.eliminar(1));
//
//        verify(repository, never()).deleteById(any());
//    }
//}
//
//
