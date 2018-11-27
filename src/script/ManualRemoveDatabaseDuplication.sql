delete
from case_executions
where iterations_iditerations not in (select iditerations from iterations);

delete from case_executions_result
where baseline_Id = "";

delete
from step_executions
where case_executions_idcase_executions not in (select idcase_executions from case_executions);

delete from step_executions_result
where baseline_Id = "";

delete
from script_executions
where step_executions_idstep_executions not in (select idstep_executions from step_executions);

delete from script_execution_result
where baseline_Id = "";

delete
from parameters_execution
where script_executions_idscript_executions not in (select idscript_executions from script_executions);