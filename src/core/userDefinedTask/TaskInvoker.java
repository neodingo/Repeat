package core.userDefinedTask;

import java.util.List;
import java.util.logging.Logger;

import core.controller.Core;
import core.keyChain.TaskActivation;

/**
 * Class to invoke tasks programmatically.
 */
public class TaskInvoker {

	private static final Logger LOGGER = Logger.getLogger(TaskInvoker.class.getName());

	private List<TaskGroup> taskGroup;

	public TaskInvoker(List<TaskGroup> taskGroup) {
		this.taskGroup = taskGroup;
	}

	/**
	 * Execute a task. Emit a warning and no-op if there is no such action.
	 *
	 * @param groupIndex
	 *            the index of the group that the task belongs to.
	 * @param taskIndex
	 *            the index of the task within the group.
	 * @throws InterruptedException
	 */
	public void execute(int groupIndex, int taskIndex) throws InterruptedException {
		execute(groupIndex, taskIndex, TaskActivation.newBuilder().build());
	}

	/**
	 * Execute a task. Emit a warning and no-op if there is
	 *
	 * @param groupIndex
	 *            the index of the group that the task belongs to.
	 * @param taskIndex
	 *            the index of the task within the group.
	 * @throws InterruptedException
	 */
	public void execute(int groupIndex, int taskIndex, TaskActivation activation) throws InterruptedException {
		if (groupIndex >= taskGroup.size()) {
			LOGGER.warning(String.format("Unable to execute task in group with index %d. There are only %d group(s).", groupIndex, taskGroup.size()));
			return;
		}
		TaskGroup group = taskGroup.get(groupIndex);

		if (taskIndex >= group.getTasks().size()) {
			LOGGER.warning(String.format("Unable to execute task in with index %d. Group %s only has %d tasks.", taskIndex, group.getName(), group.getTasks().size()));
			return;
		}
		UserDefinedAction task = group.getTasks().get(taskIndex);
		task.setInvoker(activation);
		task.trackedAction(Core.getInstance());
	}
}
