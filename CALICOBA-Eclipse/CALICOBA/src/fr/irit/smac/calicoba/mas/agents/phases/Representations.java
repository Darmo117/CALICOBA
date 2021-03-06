package fr.irit.smac.calicoba.mas.agents.phases;

import java.util.HashMap;

import fr.irit.smac.calicoba.mas.agents.messages.CriticalityMessage;

/**
 * This class abstracts the representations of satisfaction agents phases.
 * 
 * @author Damien Vergnet
 */
public class Representations extends HashMap<String, ClientRepresentation> {
  private static final long serialVersionUID = -1226332934593810290L;

  /**
   * Creates an empty representation.
   */
  public Representations() {
  }

  /**
   * Updates the representations. Adds a new step to the current phase of each
   * satisfaction agent in the requests.
   * 
   * @param requests   The requests received by the parameter agent.
   * @param agentValue The parameter agent’s current value.
   * @param worldCycle The current simulation step.
   */
  public void update(Iterable<CriticalityMessage> requests, double agentValue, int worldCycle) {
    requests.forEach(r -> {
      String id = r.getSenderName();
      if (!this.containsKey(id)) {
        this.put(id, new ClientRepresentation());
      }
      this.get(id).update(r, agentValue, worldCycle);
    });
  }

  /**
   * Estimates the delay for the given satisfaction agent.
   * 
   * @param satisfactionAgentName The satisfaction agent’s name.
   * @return The delay.
   */
  public int estimateDelay(String satisfactionAgentName) {
    if (!this.containsKey(satisfactionAgentName)) {
      throw new IllegalArgumentException(String.format("No representations for agent \"%s\".", satisfactionAgentName));
    }
    return this.get(satisfactionAgentName).estimateDelay();
  }
}
