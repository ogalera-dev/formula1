package sincronitzacio;

import sincronitzacio.excepcions.SincronitzacioException;

public interface Sincronitzador {
    public void sincronitzar() throws SincronitzacioException;
}
