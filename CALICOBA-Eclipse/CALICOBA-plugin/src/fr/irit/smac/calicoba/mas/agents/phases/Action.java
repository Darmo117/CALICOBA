package fr.irit.smac.calicoba.mas.agents.phases;

import fr.irit.smac.calicoba.mas.agents.data.Way;

/**
 * This class is an abstracion of an action that will be executed by a parameter
 * agent after a given delay.
 * 
 * @author Damien Vergnet
 */
public class Action {
  private final String beneficiaryName;
  private final Way way;
  private final int delay;
  private boolean executed;
  private int remainingStepsToWait;

  /**
   * Creates a new action.
   * 
   * @param beneficiaryName Name of the satisfaction agent that requested this
   *                        action.
   * @param way             Direction in which to update the parameter value.
   * @param delay           Delay during which all requests from beneficiaryName
   *                        have to be ignored.
   */
  public Action(String beneficiaryName, Way way, int delay) {
    this.beneficiaryName = beneficiaryName;
    this.way = way;
    this.delay = delay;
    this.remainingStepsToWait = this.delay;
  }

  /**
   * @return The name of the satisfaction agent that requested this action.
   */
  public String getBeneficiaryName() {
    return this.beneficiaryName;
  }

  /**
   * @return The direction in which to update the parameter value.
   */
  public Way getWay() {
    return this.way;
  }

  /**
   * @return True if this action has been executed, false otherwise.
   */
  public boolean isExecuted() {
    return this.executed;
  }

  /**
   * @param executed Sets this action as executed.
   */
  public void setExecuted() {
    this.executed = true;
  }

  /**
   * @return True if the wait delay has elapsed AND the action has been executed,
   *         false otherwise.
   */
  public boolean isDelayOver() {
    return this.executed && this.remainingStepsToWait == 0;
  }

  /**
   * Decreases the wait delay by 1 unit.
   */
  public void decreaseRemainingSteps() {
    this.remainingStepsToWait--;
  }

  @Override
  public String toString() {
    return String.format("Action{%s;%s;%d;%d;%s}", this.beneficiaryName, this.way, this.delay,
        this.remainingStepsToWait, this.executed ? "executed" : "");
  }
}
