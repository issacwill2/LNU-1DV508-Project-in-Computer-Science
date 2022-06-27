package cookbook.db.entities;

public class QuantifiedIngredientEntity {
    private String unit;
    private float amount;
    private IngredientEntity ingredient;
    private String name;

    public QuantifiedIngredientEntity(String unit, float amount, IngredientEntity ingredient) {
        this.unit= unit;
        if(unit == null) {
            this.unit = "";
        }
        if(unit == "unit") {
            this.unit = "";
        }
        this.amount = amount;
        this.ingredient = ingredient;
        this.name = ingredient.getName();
    }

    public QuantifiedIngredientEntity(String unit, float amount, String name) {
        this.unit= unit;
        if(unit == null) {
            this.unit = "";
        }
        this.amount = amount;
        this.name = name;
    }

    public IngredientEntity getIngredient() {
        return ingredient;
    }

    public String getUnit() {
        return unit;
    }

    public String getName() {
        return name;
    }

    public float getAmount() {
        return amount;
    }
    public void addAmount(float additional) {
        amount += additional;
    }

    public void setAmount(float amount) {
        this.amount = amount; 
    }

    @Override 
    public boolean equals(Object o) {
        if (o instanceof QuantifiedIngredientEntity) {
            QuantifiedIngredientEntity i = (QuantifiedIngredientEntity) o;
            return i.getName().equals(this.name) && i.unit.equals(this.unit);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return amount + " " + unit.toString() + " " + name;
    }

    public String toData() {
        return "INGREDIENT:" + this.amount + ":" + String.valueOf(this.unit) + ":" + this.name;
    }

}











