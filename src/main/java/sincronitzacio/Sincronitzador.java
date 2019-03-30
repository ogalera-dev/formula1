package sincronitzacio;

import sincronitzacio.excepcions.SincronitzacioException;

public interface Sincronitzador {
    public void sincLectura() throws SincronitzacioException;
    public void sincEscriptura() throws SincronitzacioException;
    public void fiEscriptura() throws SincronitzacioException;
}
