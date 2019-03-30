package es.entrada.estrategies;

import models.Registre;

import java.io.Closeable;

public interface EstrategiaLectura extends Closeable {
    public Registre llegir();
}