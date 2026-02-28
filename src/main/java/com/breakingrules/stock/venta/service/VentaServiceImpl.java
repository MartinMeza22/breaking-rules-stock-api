package com.breakingrules.stock.venta.service;

import com.breakingrules.stock.clientes.repository.ClienteRepository;
import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.productos.repository.ProductoRepository;
import com.breakingrules.stock.venta.dto.ItemVentaDTO;
import com.breakingrules.stock.venta.dto.VentaDTO;
import com.breakingrules.stock.venta.entity.Venta;
import com.breakingrules.stock.venta.entity.VentaDetalle;
import com.breakingrules.stock.venta.repository.VentaDetalleRepository;
import com.breakingrules.stock.venta.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepo;
    private final VentaDetalleRepository ventaDetalleRepo;
    private final ProductoRepository productoRepo;
    private final ClienteRepository clienteRepo;

    @Override
    @Transactional
    public void confirmarVenta(VentaDTO dto) {

        Venta venta = new Venta();
        venta.setFecha(LocalDateTime.now());
        venta.setFormaPago(dto.getFormaPago());
        venta.setCliente(clienteRepo.findById(dto.getClienteId()).orElseThrow());

        ventaRepo.save(venta);

        BigDecimal total = BigDecimal.ZERO;

        for (ItemVentaDTO item : dto.getItems()) {
            Producto producto = productoRepo.findById(item.getProductoId()).orElseThrow();

            if (producto.getStock() < item.getCantidad()) throw new RuntimeException("Stock insuficiente");

            VentaDetalle det = new VentaDetalle();
            det.setVenta(venta);
            det.setProducto(producto);
            det.setCantidad(item.getCantidad());
            det.setPrecioUnitario(item.getPrecioUnitario());

            BigDecimal subtotal = item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad()));
            det.setSubtotal(subtotal);

            ventaDetalleRepo.save(det);

            producto.setStock(producto.getStock() - item.getCantidad());

            total = total.add(subtotal);
        }
        venta.setTotal(total);
        venta.setVuelto(dto.getMontoPagado().subtract(total));
        ventaRepo.save(venta);
    }
}
