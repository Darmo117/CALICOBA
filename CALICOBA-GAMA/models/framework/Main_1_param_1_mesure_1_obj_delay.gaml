model Main

import "target_models/1_param_1_mesure_1_obj_delay.gaml"

global skills: [calicoba] {
  TargetModel target_model;

  init {
    do calicoba_init();

    create TargetModel number: 1 returns: target;
    target_model <- first(target);

    do calicoba_setup();
  }

  reflex step {
    do calicoba_step();
  }
}

experiment Experiment type: gui {
  output {
    display "Time Series" {
      chart "Parameters" type: series size: {0.5, 0.5} position: {0, 0.5} {
        data "p1" value: target_model.param_1 color: #red marker: false;
        data "m1" value: target_model.out_1 color: #green marker: false;
      }

      chart "Parameters’ Actions" type: series size: {0.5, 0.5} position: {0.5, 0.5} {
        data "p1" value: target_model.get_parameter_action("param_1") color: #black marker: false;
      }

      chart "Criticalities" type: series size: {0.5, 0.5} position: {0, 0} {
        data "obj_1" value: world.get_objective("obj_1") color: #green marker: false;
      }
    }
  }
}
