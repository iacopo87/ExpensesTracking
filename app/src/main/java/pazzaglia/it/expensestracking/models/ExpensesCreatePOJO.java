package pazzaglia.it.expensestracking.models;

/**
 * Created by C305445 on 6/27/2016.
 */

import java.util.HashMap;
import java.util.Map;


public class ExpensesCreatePOJO {

    private Boolean error;
    private String message;
    private Integer expenseId;
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
     * The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     * The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return
     * The expenseId
     */
    public Integer getExpenseId() {
        return expenseId;
    }

    /**
     *
     * @param expenseId
     * The expense_id
     */
    public void setExpenseId(Integer expenseId) {
        this.expenseId = expenseId;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}