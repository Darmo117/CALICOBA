package fr.irit.smac.calicoba.mas;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import fr.irit.smac.calicoba.mas.agents.Agent;

/**
 * The world within which agents live and behave.
 * 
 * @author Damien Vergnet
 */
public class World {
  /** The list of last attributed IDs for each agent type. */
  private Map<Class<? extends Agent>, Integer> globalIds;
  /** List of all alive agents in insertion order. */
  private List<Agent> orderedAgents;
  /** Registry of all alive agents by type. */
  private Map<Class<? extends Agent>, List<Agent>> agentsRegistry;
  /** Registry of all alive agents by ID. */
  private Map<String, Agent> agentsIdsRegistry;

  /** Agents scheduling order. */
  private List<Class<? extends Agent>> customSchedule;

  /**
   * Creates an empty world with creation order scheduling.
   */
  public World() {
    this(null);
  }

  /**
   * Creates an empty world with custom agents scheduling.
   * 
   * @param customSchedule A list defining the order in which each agent type has
   *                       to be executed. Only works if first argument is equal
   *                       to {@link SchedulingType#TYPE}.
   */
  protected World(List<Class<? extends Agent>> customSchedule) {
    this.globalIds = new HashMap<>();
    this.orderedAgents = new LinkedList<>();
    this.agentsRegistry = new HashMap<>();
    this.agentsIdsRegistry = new HashMap<>();

    this.customSchedule = customSchedule != null ? Collections.unmodifiableList(customSchedule) : null;
  }

  /**
   * Returns a list of all alive agents of the given type.
   * 
   * @param <T>  Agents’ type.
   * @param type Agents’ class.
   * @return The list of agents for the given type.
   */
  @SuppressWarnings("unchecked")
  public <T extends Agent> List<T> getAgentsForType(Class<T> type) {
    return (List<T>) this.agentsRegistry.get(type);
  }

  /**
   * Return the agent with the given ID.
   * 
   * @param agentId The agent’s ID.
   * @return The agent or null if not match.
   */
  public Agent getAgentById(String agentId) {
    return this.agentsIdsRegistry.get(agentId);
  }

  /**
   * Returns the first agent matching the given filter.
   * 
   * @param filter The filter the agent has to match.
   * @return The first agent that fulfills the filter, or null if none did.
   */
  public Agent getAgent(Predicate<Agent> filter) {
    return this.orderedAgents.stream().filter(filter).findFirst().orElse(null);
  }

  /**
   * Adds an agent to this world and sets its ID.
   * 
   * @param agent The agent to add.
   */
  public void addAgent(Agent agent) {
    Class<? extends Agent> agentClass = agent.getClass();

    if (!this.globalIds.containsKey(agentClass)) {
      this.globalIds.put(agentClass, 0);
    }
    this.globalIds.put(agentClass, this.globalIds.get(agentClass) + 1);

    if (!this.agentsRegistry.containsKey(agentClass)) {
      this.agentsRegistry.put(agentClass, new LinkedList<>());
    }
    this.agentsRegistry.get(agentClass).add(agent);

    this.agentsIdsRegistry.put(agent.getId(), agent);
    this.orderedAgents.add(agent);

    agent.setId(String.format("%s_%d", agentClass.getSimpleName(), this.globalIds.get(agentClass)));
    agent.setWorld(this);
  }

  /**
   * Runs the simulation for 1 step. If a custom schedule has been set, for each
   * agent type, all agents of said type perceive their environment then decides
   * and acts. Otherwise, all agents (in creation order) perceive their
   * environment then they decide and act.
   */
  public void step() {
    if (this.customSchedule == null) {
      this.orderedAgents.forEach(Agent::perceive);
      this.orderedAgents.forEach(Agent::decideAndAct);
    }
    else {
      this.customSchedule.forEach(type -> {
        List<Agent> agents = this.agentsRegistry.get(type);
        agents.forEach(Agent::perceive);
        agents.forEach(Agent::decideAndAct);
      });
    }
  }
}