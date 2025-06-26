public enum Dificultad {
    FACIL(2, 100),
    MEDIO(3, 60),
    DIFICIL(4, 40);

    public final int velocidadEnemigo;
    public final int tasaSpawn;

    Dificultad(int velocidadEnemigo, int tasaSpawn) {
        this.velocidadEnemigo = velocidadEnemigo;
        this.tasaSpawn = tasaSpawn;
    }
}