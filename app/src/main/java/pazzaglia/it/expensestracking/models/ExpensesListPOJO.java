package pazzaglia.it.expensestracking.models;

/**
 * Created by C305445 on 6/27/2016.
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpensesListPOJO {

    private Boolean error;
    private List<Expense> expenses = new ArrayList<Expense>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The error
     */
    public Boolean getError() {
        return error;
    }

    /**
     *
     * @param error
     * The error
     */
    public void setError(Boolean error) {
        this.error = error;
    }

    /**
     *
     * @return
     * The expenses
     */
    public List<Expense> getExpenses() {
        return expenses;
    }

    /**
     *
     * @param expenses
     * The expenses
     */
    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
