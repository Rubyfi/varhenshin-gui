package org.eclipse.emf.henshin.variability.configuration.ui.parts;

import org.eclipse.emf.henshin.variability.configuration.ui.helpers.ImageHelper;
import org.eclipse.emf.henshin.variability.ui.viewer.util.VPViewerBindingEditingSupport;
import org.eclipse.emf.henshin.variability.ui.viewer.util.VPViewerComparator;
import org.eclipse.emf.henshin.variability.ui.viewer.util.VPViewerContentProvider;
import org.eclipse.emf.henshin.variability.ui.viewer.util.VPViewerNameEditingSupport;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;

import configuration.VariabilityPoint;

public class ConfigurationTableViewer extends SynchronizedTableViewer {


	private VPViewerComparator comparator;
	
	public ConfigurationTableViewer(Composite parent, int style, TableColumnLayout tableColumnLayout, ITableViewerSynchronizedPart synchronizedPart) {
		super(parent, style, synchronizedPart);
		createColumns(parent, tableColumnLayout);
		getTable().setHeaderVisible(true);
		getTable().setLinesVisible(true);
		setContentProvider(new VPViewerContentProvider());
		comparator = new VPViewerComparator();
		setComparator(comparator);
	}
		
	private void createColumns(final Composite parent, final TableColumnLayout tableColumnLayout) {
		String[] titles = { "Variability Point", "Binding" };

		TableViewerColumn col = createTableViewerColumn(titles[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				VariabilityPoint vp = (VariabilityPoint) element;
				return vp.getName();
			}
			@Override
			public Image getImage(Object element) {
				return ImageHelper.getImage("/icons/table_default.png");
			}
		});
		col.setEditingSupport(new VPViewerNameEditingSupport(this));
		tableColumnLayout.setColumnData(col.getColumn(), new ColumnWeightData(60, false));

		col = createTableViewerColumn(titles[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				VariabilityPoint vp = (VariabilityPoint) element;
				return vp.getBinding().getName();
			}
			@Override
			public Image getImage(Object element) {
				return ImageHelper.getImage("/icons/table_default.png");
				//return ImageHelper.getImage("/icons/" +  ((VariabilityPoint) element).getBinding().getName().toLowerCase() +  ".png");
			}
		});
		col.setEditingSupport(new VPViewerBindingEditingSupport(this));
		tableColumnLayout.setColumnData(col.getColumn(), new ColumnWeightData(40, false));
	}

	private TableViewerColumn createTableViewerColumn(String title, int index) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(this, SWT.NONE, index);
		final TableColumn column = viewerColumn.getColumn();

		column.setText(title);
		column.setResizable(false);
		column.setMoveable(false);

		column.addSelectionListener(getSelectionAdapter(column, index));

		return viewerColumn;
	}

	private SelectionAdapter getSelectionAdapter(final TableColumn column, final int index) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comparator.setColumn(index);
				int sortDirection = comparator.getDirection();
				getTable().setSortDirection(sortDirection);
				getTable().setSortColumn(column);
				refresh();
			}
		};
		return selectionAdapter;
	}	
}
