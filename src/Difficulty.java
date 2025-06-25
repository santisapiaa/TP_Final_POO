public enum Difficulty {
    FACIL(2, 100),
    MEDIO(3, 60),
    DIFICIL(4, 40);

    public final int enemySpeed;
    public final int spawnRate;

    Difficulty(int enemySpeed, int spawnRate) {
        this.enemySpeed = enemySpeed;
        this.spawnRate = spawnRate;
    }
}