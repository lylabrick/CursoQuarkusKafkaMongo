package com.repopublicaciones.dto;

import com.repopublicaciones.model.TipoPublicacion;

public class EstadisticaTipo {

    public TipoPublicacion tipo;
    public long cantidad;

    public EstadisticaTipo() {
    }

    public EstadisticaTipo(TipoPublicacion tipo, long cantidad) {
        this.tipo = tipo;
        this.cantidad = cantidad;
    }
}