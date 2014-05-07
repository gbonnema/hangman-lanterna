package jcurses.widgets;

/******************************************************************************
 * 
 * This interface must be implemented by layout managers that layout widgets
 * within widget containers.
 */

public interface LayoutManager {

	/**
	 * The method layouts a widget within a container, dependent on layout
	 * constraints.
	 * 
	 * @param child
	 *            widget to layout
	 * @param constraints
	 *            layout constraints
	 */
	void layout(Widget child, Object constraints);

	/**
	 * The method is called by framework to bind the layout manager to a
	 * container. The implementing class must recognize whether this is already
	 * bound or not, and throw an exception if it tries to bind it second time.
	 * This is because the framework restricts a layout manager to be bound only
	 * to one container at a time.
	 * 
	 * @param container
	 *            container to bind
	 */
	void bindToContainer(WidgetContainer container);

	/**
	 * The method is called by framework to unbind the layout manager from its
	 * container. The implementing class must recognize whether this is already
	 * bound or not, and throw an exception if it tries to bind it second time.
	 * This is because the framework restricts a layout manager to be bound only
	 * to one container at a time.
	 * 
	 */

	void unbindFromContainer();

	/**
	 * Adds a widget to the container.
	 * 
	 * @param widget
	 *            widget to be added
	 * @param x
	 *            location x
	 * @param y
	 *            location y
	 * @param width
	 *            width
	 * @param height
	 *            height
	 * @param verticalConstraint
	 *            one of the WidgetConstants
	 * @param horizontalConstraint
	 *            one of the WidgetConstants
	 */

	void addWidget(Widget widget, int x, int y, int width, int height,
			int verticalConstraint, int horizontalConstraint);

	/**
	 * Removes a widget from the container.
	 * 
	 * @param widget
	 *            widget to be removed
	 */

	void removeWidget(Widget widget);
}