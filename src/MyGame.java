import java.util.Random;

public class MyGame {
    private static final int bossDamage = 50;
    private static final int GOLEM_DAMAGE_BY_BOSS = bossDamage * 1 / 5;
    private static final int HERO_DAMAGE_BY_BOSS = bossDamage * 4 / 5;
    private static final Random RANDOM = new Random();
    private static final int[] heroesHealth = {280, 270, 250, 250, 200, 210, 300, 180};
    private static final int[] heroesDamage = {10, 15, 20, 0, 20, 15, 5, 25};
    private static final String[] heroesAttackType = {"Physical", "Magical", "Kinetic", "Medic", "Lucky", "Berserk", "Golem", "Thor"};
    private static int bossHealth = 1000;
    private static String bossDefence;
    private static int roundNumber = 0;
    private static int damageAccumulatedByBerserk = 0;
    private static final int golemIndex = 6;
    private static boolean isBossDazed = false;


    public static void main(String[] args) {
        printStatistics();
        while (!isGameFinished()) {
            playRound();
        }
    }

    private static void playRound() {
        System.out.println("ROUND " + ++roundNumber + " ---------------");
        chooseBossDefence();
        bossHits();
        heroesHit();
        printStatistics();
    }

    private static void healHero(int medicIndex) {
        if (heroesHealth[medicIndex] <= 0) {
            System.out.println("Medic was dead");
            return;
        }
        boolean isHealed = false;
        int count = 0;
        while (!isHealed && count < heroesHealth.length) {
            int heroIndex = RANDOM.nextInt(0, heroesHealth.length);
            if (heroIndex != medicIndex && heroesHealth[heroIndex] > 0 && heroesHealth[heroIndex] < 100) {
                System.out.print("Hero had " + heroesHealth[heroIndex] + " and after healing got ");
                heroesHealth[heroIndex] += RANDOM.nextInt(100);
                System.out.println(heroesHealth[heroIndex]);

                isHealed = true;
            }
            count++;
        }

    }
    private static void chooseBossDefence() {
        int randomIndex = RANDOM.nextInt(heroesAttackType.length); // 0,1,2
        bossDefence = heroesAttackType[randomIndex];
    }

    private static void heroesHit() {
        for (int i = 0; i < heroesDamage.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0) {
                int damage = heroesDamage[i];
                if (isHeroMedic(i)) {
                    healHero(i);
                    continue;
                }
                if (bossDefence == heroesAttackType[i]) {
                    int coefficient = RANDOM.nextInt(8) + 2; // 2,3,4,5,6,7,8,9
                    damage = damage * coefficient;
                    System.out.println("Critical damage for " + heroesAttackType[i] + ": " + damage);
                }
                if (isBerserk(i)) {
                    bossHealth -= (damage + damageAccumulatedByBerserk);
                } else {
                    bossHealth -= damage;
                }
                if (isThorLucky(i)) {
                    isBossDazed = true;
                    System.out.println("Thor was lucky and next round boss would be dazed");
                }
                if (bossHealth < 0) {
                    bossHealth = 0;
                }
            }
        }
    }

    private static void bossHits() {
        if (isBossDazed) {
            isBossDazed = false;
            return;
        }
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                if (isLuckyLucky(i)) {
                    System.out.println("Lucky was lucky this round and avoided boss hit");
                    continue;
                }
                if (isGolem(i)) {
                    heroesHealth[i] -= bossDamage;
                } else if (heroesHealth[golemIndex] - GOLEM_DAMAGE_BY_BOSS > 0){
                    heroesHealth[golemIndex] -= GOLEM_DAMAGE_BY_BOSS;
                    heroesHealth[i] -= HERO_DAMAGE_BY_BOSS;
                } else {
                    heroesHealth[i] -= bossDamage;
                }
                if (heroesHealth[i] < 0) {
                    heroesHealth[i] = 0;
                }
                if (isBerserk(i)) {
                    damageAccumulatedByBerserk = RANDOM.nextInt(bossDamage);
                    System.out.println("Berserk attacked with accumulated power: " + damageAccumulatedByBerserk);
                }
            }
        }
    }

    private static boolean isBerserk(int i) {
        return "Berserk" == heroesAttackType[i];
    }

    private static boolean isThorLucky(int i) {
        return "Thor" == heroesAttackType[i] && RANDOM.nextBoolean();
    }

    private static boolean isGolem(int i) {
        return "Golem" == heroesAttackType[i];
    }

    private static boolean isHeroMedic(int i) {
        return "Medic" == heroesAttackType[i];
    }

    private static boolean isLuckyLucky(int i) {
        return "Lucky" == heroesAttackType[i] && RANDOM.nextBoolean();
    }

    private static boolean isGameFinished() {
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }
        boolean allHeroesDead = true;
        for (int j : heroesHealth) {
            if (j > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead) {
            System.out.println("Boss won!!!");
        }
        return allHeroesDead;
    }

    private static void printStatistics() {
        System.out.println("--*** ROUND STATS ***--");
        System.out.println("Boss health: " + bossHealth + " damage: " + bossDamage + " defence: "
                + (bossDefence == null ? "No defence" : bossDefence));
        for (int i = 0; i < heroesHealth.length; i++) {
            System.out.println(heroesAttackType[i] + " health: " + heroesHealth[i]
                    + " damage: " + heroesDamage[i]);
        }
        System.out.println();
    }
}
