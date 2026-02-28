package com.breakingrules.stock.venta.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VentaDTO {
    private Integer clienteId;
    private String formaPago;
    private List<ItemVentaDTO> items  = new ArrayList<>();
    private BigDecimal montoPagado;
    private BigDecimal vuelto;

    public VentaDTO() {
    }


    public VentaDTO(Integer clienteId, BigDecimal montoPagado, BigDecimal vuelto, List<ItemVentaDTO> items, String formaPago) {
        this.clienteId = clienteId;
        this.montoPagado = montoPagado;
        this.vuelto = vuelto;
        this.items = items;
        this.formaPago = formaPago;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public List<ItemVentaDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemVentaDTO> items) {
        this.items = items;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public BigDecimal getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(BigDecimal montoPagado) {
        this.montoPagado = montoPagado;
    }

    public BigDecimal getVuelto() {
        return vuelto;
    }

    public void setVuelto(BigDecimal vuelto) {
        this.vuelto = vuelto;
    }
}
