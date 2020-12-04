public class Legend {
    private String nickname;
    private String name;
    private String gender;
    private String age;
    private String weight;
    private String height;
    private String type;
    private String home_world;

    public Legend(String nickname, String name, String gender, String age, String weight, String height, String type, String home_world){
        this.nickname = nickname;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.type = type;
        this.home_world = home_world;
    }

    @Override
    public String toString() {
        return "Legenda{\n" +
                " Pseudonim: " + nickname  +
                ",\n Imię i nazwisko: " + name  +
                ",\n Płeć: " + gender +
                ",\n Wiek: " + age  +
                ",\n Waga: " + weight +
                ",\n Wzrost: " + height +
                ",\n Klasa: " + type  +
                ",\n Pochodzenie: " + home_world  +'\n' +
                '}';
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHome_world() {
        return home_world;
    }

    public void setHome_world(String home_world) {
        this.home_world = home_world;
    }
}
