/*
 * Copyright (C) 2013 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opendatakit.tables.views.webkits;

import java.lang.ref.WeakReference;

import org.opendatakit.common.android.utilities.WebLogger;
import org.opendatakit.tables.R;

import android.os.RemoteException;
import android.widget.Toast;

/**
 * This object is handed to all the javascript views as "control".
 * 
 * @author mitchellsundt@gmail.com
 * @author sudar.sam@gmail.com
 *
 */
public class ControlIf {

  public static final String TAG = "ControlIf";
  
  private WeakReference<Control> weakControl;

  ControlIf(Control control) {
    weakControl = new WeakReference<Control>(control);
  }

  /**
   * Open the table with the given id.
   *
   * This synchronously launches an intent to open the table in its default view.
   * 
   * @see #query(String, String, String[])
   * @param tableId
   *          the table id of the table to open
   * @param whereClause
   *          query as specified by {@link #query(String, String, String[])} If
   *          null will not restrict the results.
   * @param selectionArgs
   *          an array of selection arguments, one for each "?" in whereClause.
   *          If null will not restrict the results.
   * @return true if the open succeeded
   */
  @android.webkit.JavascriptInterface
  public boolean openTable(String tableId, String whereClause, String[] selectionArgs) {
    // TODO: convert to element keys
    return weakControl.get().helperOpenTable(tableId, whereClause, selectionArgs, null, null, null,
        null);
  }

  /**
   * Open the given table with the given list view, restricted by given query.
   *
   * This synchronously launches an intent to open the table in its list view.
   *
   * @see #query(String, String, String[])
   * @see #openTable(String, String, String[])
   * @param tableId
   *          the tableId of the table to open
   * @param whereClause
   *          query as specified by {@link #query(String, String, String[])} If
   *          null will not restrict the results.
   * @param selectionArgs
   *          an array of selection arguments, one for each "?" in whereClause.
   *          If null will not restrict the results.
   * @param relativePath
   *          the name of the file specifying the list view, relative to the app
   *          folder.
   * @return true if the open succeeded
   */
  @android.webkit.JavascriptInterface
  public boolean openTableToListView(String tableId, String whereClause, String[] selectionArgs,
      String relativePath) {
    return weakControl.get().helperOpenTableWithFile(tableId, relativePath, whereClause,
        selectionArgs, null, null, null, null);
  }

  /**
   * Open the given table to the map view, restricted with the given SQL query.
   *
   * This synchronously launches an intent to open the table in its map view.
   *
   * @see #query(String, String, String[])
   * @see #openTable(String, String, String[])
   * @param tableId
   *          the tableId of the table to open
   * @param whereClause
   *          query as specified by {@link #query(String, String, String[])} If
   *          null will not restrict the results.
   * @param selectionArgs
   *          an array of selection arguments, one for each "?" in whereClause.
   *          If null will not restrict the results.
   * @param relativePath
   *          NOT YET SUPPORTED
   * @return true if the open succeeded
   */
  @android.webkit.JavascriptInterface
  public boolean openTableToMapView(String tableId, String whereClause, String[] selectionArgs,
      String relativePath) {
    return weakControl.get().helperOpenTableToMapView(tableId, relativePath, whereClause,
        selectionArgs, null, null, null, null);
  }

  /**
   * Open the table to spreadsheet view, restricting by the given SQL query.
   *
   * This synchronously launches an intent to open the table in its java spreadsheet view.
   *
   * @see #query(String, String, String[])
   * @see #openTable(String, String, String[])
   * @param tableId
   *          the tableId of the table to open
   * @param whereClause
   *          query as specified by {@link #query(String, String, String[])} If
   *          null will not restrict the results.
   * @param selectionArgs
   *          an array of selection arguments, one for each "?" in whereClause.
   *          If null will not restrict the results.
   * @return true if the open succeeded
   */
  @android.webkit.JavascriptInterface
  public boolean openTableToSpreadsheetView(String tableId, String whereClause,
      String[] selectionArgs) {
    return weakControl.get().helperOpenTableToSpreadsheetView(tableId, whereClause, selectionArgs,
        null, null, null, null);
  }

  /**
   * CHANGE THIS TO ASYNC
   * CHANGE THIS TO ASYNC
   * CHANGE THIS TO ASYNC
   * CHANGE THIS TO ASYNC
   * CHANGE THIS TO ASYNC
   * CHANGE THIS TO ASYNC
   * CHANGE THIS TO ASYNC
   * CHANGE THIS TO ASYNC
   * CHANGE THIS TO ASYNC
   *
   * Return a {@link TableDataIf} object for the given table id, where the rows
   * have been restricted by the query.
   * <p>
   * For example, if you wanted all the rows where the column with elementKey
   * foo equaled bar, the where clause would be "foo = ? ", and the selection
   * args would be ["bar"].
   * <p>
   * If you require only those rows where foo equals bar and foo2 = bar2, you
   * can combine these restrictions using "AND". For instance, this query would
   * have a where clause of "foo = ? AND foo2 = ?" while the selectionArgs would
   * be ["bar", "bar2"].
   * <p>
   * The query also supports OR operations, so you can modify the above query to
   * include rows where foo equals bar or those where foo2 = bar2, you can say
   * "foo = ? OR foo2 = ?" and use the same selectionArgs.
   * <p>
   * If you need more complicated queries joining tables, this can also be
   * achieved using this method and SQL syntax. The {@link TableDataIf} returned
   * by this method can only contain those columns present in the table
   * specified by tableId. This means that you cannot join tables and retrieve
   * the entire projection. You can, however, perform subselections and other
   * valid SQL queries, so long as you do not require additional columns in the
   * projection.
   * <p>
   * This can be used to do powerful cross-table queries.
   * 
   * @param tableId
   *          the tableId of the table
   * @param whereClause
   *          a where clause as described above. Must include "?" instead of
   *          actual values, which are instead passed in the sqlSelectionArgs
   *          parameter. The references to tables must use the table ids. The
   *          references to the columns must be the element keys
   * @param selectionArgs
   *          an array of selection arguments, one for each "?" in
   *          sqlWhereClause
   * @return a new TableDataIf with the results of the query. Should be released
   *         with {@link #releaseQueryResources(String)} when no longer needed.
   */
  @android.webkit.JavascriptInterface
  public TableDataIf query(String tableId, String whereClause, String[] selectionArgs) {
    TableData td;
    try {
      td = weakControl.get().query(tableId, whereClause, selectionArgs, null, null, null, null);
      if (td != null) {
        return td.getJavascriptInterfaceWithWeakReference();
      } else {
        return null;
      }
    } catch (RemoteException e) {
      String appName = weakControl.get().retrieveAppName();
      WebLogger.getLogger(appName).printStackTrace(e);
      WebLogger.getLogger(appName).e(TAG, "Error accessing database: " + e.toString());
      Toast.makeText(weakControl.get().mActivity, R.string.error_accessing_database, Toast.LENGTH_LONG).show();
      return null;
    }
  }

  /**
   * Releases the results returned from the query() and queryWithSql()
   * statements, above. The object will be retained until this method is called,
   * so good practice is to release the query when its data is no longer needed
   * to free up resources.
   * 
   * @param tableId
   *          tableId of the table
   */
  @android.webkit.JavascriptInterface
  public void releaseQueryResources(String tableId) {
    weakControl.get().releaseQueryResources(tableId);
  }

  /**
   * Get the table ids of all the tables in the database.
   * 
   * @return a stringified json array of the table ids
   */
  @android.webkit.JavascriptInterface
  public String getAllTableIds() {
    return weakControl.get().getAllTableIds();
  }

  /**
   * Launch an arbitrary HTML file specified by filename.
   *
   * This synchronously launches an intent to open the URL.
   *
   * @param relativePath
   *          file name relative to the ODK Tables folder.
   * @return true if the file was launched, false if something went wrong
   */
  @android.webkit.JavascriptInterface
  public boolean launchHTML(String relativePath) {
    return weakControl.get().launchHTML(relativePath);
  }

  /**
   * Open the item specified by the index to the detail view.
   *
   * This synchronously launches an intent to open the rowId in its detail view
   * <p>
   * The relativePath parameter is optional, and if null an attempt will be made
   * to use the default file.
   * 
   * @param tableId
   * @param rowId
   * @param relativePath
   *          the name of the file specifying the detail view, relative to the
   *          app folder. If not present, the default detail view file will be
   *          used
   * @return true if the open succeeded
   */
  @android.webkit.JavascriptInterface
  public boolean openDetailView(String tableId, String rowId, String relativePath) {
    return weakControl.get().openDetailViewWithFile(tableId, rowId, relativePath);
  }

  /**
   * Add a row using Collect and the default form.
   *
   * This synchronously launches an intent to open ODK Collect to insert a new row in the table.
   *
   * @param tableId
   *          the tableId of the table to receive the add.
   * @return true if the activity was launched, false if something went wrong
   * @deprecated
   */
  @android.webkit.JavascriptInterface
  public boolean addRowWithCollectDefault(String tableId) {
    return this.addRowWithCollect(tableId, null, null, null, null);
  }

  /**
   * Add a row using Collect, a specific form, and a map of prepopulated values.
   *
   * This synchronously launches an intent to open ODK Collect to insert a new row in the table.
   * <p>
   * The form must have been added to Collect and visible in the "Fill Blank
   * Forms" screen.
   * 
   * @param tableId
   * @param formId
   *          if null, will launch the default form
   * @param formVersion
   * @param formRootElement
   * @param jsonMap
   *          a JSON map of element key to value, as retrieved by
   *          {@link #getElementKey(String, String)}. The map can then be
   *          converted to a String using JSON.stringify() and passed to this
   *          method. A null value will not prepopulate any values.
   * @return true if the activity was launched, false if something went wrong
   * @deprecated
   */
  @android.webkit.JavascriptInterface
  public boolean addRowWithCollect(String tableId, String formId, String formVersion,
      String formRootElement, String jsonMap) {
    try {
      return weakControl.get().helperAddRowWithCollect(tableId, formId, formVersion, formRootElement,
          jsonMap);
    } catch (RemoteException e) {
      String appName = weakControl.get().retrieveAppName();
      WebLogger.getLogger(appName).printStackTrace(e);
      WebLogger.getLogger(appName).e(TAG, "Error accessing database: " + e.toString());
      Toast.makeText(weakControl.get().mActivity, R.string.error_accessing_database, Toast.LENGTH_LONG).show();
      return false;
    }
  }

  /**
   * Edit the given row using Collect.
   *
   * This synchronously launches an intent to open ODK Collect to edit a row in the table.
   *
   * @param tableId
   * @param rowId
   * @return true if the activity was launched, false if something went wrong
   * @deprecated
   */
  @android.webkit.JavascriptInterface
  public boolean editRowWithCollectDefault(String tableId, String rowId) {
    return this.editRowWithCollect(tableId, rowId, null, null, null);
  }

  /**
   * Edit the given row using Collect and a specific form.
   *
   * This synchronously launches an intent to open ODK Collect to edit a row in the table.
   *
   * @param tableId
   * @param rowId
   * @param formId
   *          if null, uses the default form
   * @param formVersion
   * @param formRootElement
   * @return true if the activity was launched, false if something went wrong
   * @deprecated
   */
  @android.webkit.JavascriptInterface
  public boolean editRowWithCollect(String tableId, String rowId, String formId,
      String formVersion, String formRootElement) {
    try {
      return weakControl.get().helperEditRowWithCollect(tableId, rowId, formId, formVersion,
          formRootElement);
    } catch (RemoteException e) {
      String appName = weakControl.get().retrieveAppName();
      WebLogger.getLogger(appName).printStackTrace(e);
      WebLogger.getLogger(appName).e(TAG, "Error accessing database: " + e.toString());
      Toast.makeText(weakControl.get().mActivity, R.string.error_accessing_database, Toast.LENGTH_LONG).show();
      return false;
    }
  }

  /**
   * Edit the given row using Survey and the default form.
   *
   * This synchronously launches an intent to open ODK Survey to edit a row in the table.
   *
   * @param tableId
   * @param rowId
   * @return true if the activity was launched, false if something went wrong
   */
  @android.webkit.JavascriptInterface
  public boolean editRowWithSurveyDefault(String tableId, String rowId) {
    return editRowWithSurvey(tableId, rowId, null, null);
  }

  /**
   * Edit the given row using Survey and a specific form.
   *
   * This synchronously launches an intent to open ODK Survey to edit a row in the table.
   *
   * @param tableId
   * @param rowId
   * @param formId
   * @param screenPath
   * @return true if the activity was launched, false if something went wrong
   */
  @android.webkit.JavascriptInterface
  public boolean editRowWithSurvey(String tableId, String rowId, String formId, String screenPath) {
    try {
      return weakControl.get().helperEditRowWithSurvey(tableId, rowId, formId, screenPath);
    } catch (RemoteException e) {
      String appName = weakControl.get().retrieveAppName();
      WebLogger.getLogger(appName).printStackTrace(e);
      WebLogger.getLogger(appName).e(TAG, "Error accessing database: " + e.toString());
      Toast.makeText(weakControl.get().mActivity, R.string.error_accessing_database, Toast.LENGTH_LONG).show();
      return false;
    }
  }

  /**
   * Add a row with Survey and the default form.
   *
   * This synchronously launches an intent to open ODK Survey to add a row in the table.
   *
   * @param tableId
   *          the table to receive the add
   * @return true if Survey was launched, else false
   */
  @android.webkit.JavascriptInterface
  public boolean addRowWithSurveyDefault(String tableId) {
    return this.addRowWithSurvey(tableId, null, null, null);
  }

  /**
   * Add a row using Survey.
   *
   * This synchronously launches an intent to open ODK Survey to add a row in the table.
   *
   * @param tableId
   * @param formId
   *          if null, the default form will be used
   * @param screenPath
   * @param jsonMap
   *          a stringified json object matching element key to the value to
   *          prepopulate in the new row
   * @return true if the activity was launched, false if something went wrong
   */
  @android.webkit.JavascriptInterface
  public boolean addRowWithSurvey(String tableId, String formId, String screenPath, String jsonMap) {
    try {
      return weakControl.get().helperAddRowWithSurvey(tableId, formId, screenPath, jsonMap);
    } catch (RemoteException e) {
      String appName = weakControl.get().retrieveAppName();
      WebLogger.getLogger(appName).printStackTrace(e);
      WebLogger.getLogger(appName).e(TAG, "Error accessing database: " + e.toString());
      Toast.makeText(weakControl.get().mActivity, R.string.error_accessing_database, Toast.LENGTH_LONG).show();
      return false;
    }
  }

  /**
   * Return the element key for the column with the given element path.
   * 
   * @param tableId
   * @param elementPath
   * @return the element key for the column, or null if a table cannot be found
   *         with the existing tableId.
   */
  @android.webkit.JavascriptInterface
  public String getElementKey(String tableId, String elementPath) {
    return weakControl.get().getElementKey(tableId, elementPath);
  }

  /**
   * Get the display name for the given column.
   * 
   * @param tableId
   * @param elementPath
   * @return the display name for the given column
   */
  @android.webkit.JavascriptInterface
  public String getColumnDisplayName(String tableId, String elementPath) {
    try {
      return weakControl.get().getColumnDisplayName(tableId, elementPath);
    } catch (RemoteException e) {
      String appName = weakControl.get().retrieveAppName();
      WebLogger.getLogger(appName).printStackTrace(e);
      WebLogger.getLogger(appName).e(TAG, "Error accessing database: " + e.toString());
      Toast.makeText(weakControl.get().mActivity, R.string.error_accessing_database, Toast.LENGTH_LONG).show();
      return elementPath;
    }
  }

  /**
   * Retrieve the display name for the given table.
   * <p>
   * If the display name has been localized, it returns the json representation
   * of the display name.
   * 
   * @param tableId
   * @return the display name for the table, in stringified json form if the
   *         name has been internationalized
   */
  @android.webkit.JavascriptInterface
  public String getTableDisplayName(String tableId) {
    try {
      return weakControl.get().getTableDisplayName(tableId);
    } catch (RemoteException e) {
      String appName = weakControl.get().retrieveAppName();
      WebLogger.getLogger(appName).printStackTrace(e);
      WebLogger.getLogger(appName).e(TAG, "Error accessing database: " + e.toString());
      Toast.makeText(weakControl.get().mActivity, R.string.error_accessing_database, Toast.LENGTH_LONG).show();
      return tableId;
    }
  }

  /**
   * CHANGE TO ASYNC
   * CHANGE TO ASYNC
   * CHANGE TO ASYNC
   * CHANGE TO ASYNC
   * CHANGE TO ASYNC
   * CHANGE TO ASYNC
   *
   * Add a row to the table. Fails if the table cannot be found, if a column in
   * the stringifiedObject cannot be found, or if a value cannot be parsed to
   * the correct type.
   * <p>
   * Usage is the following:\n <code>
   * var valuesMap = {};<br>
   * valuesMap['stringElementKey'] = 'howdy ho';<br>
   * valuesMap['elementKeyTwo'] = '2.3351';<br>
   * var stringifiedObject = JSON.stringify(valuesMap);<br>
   * control.addRow('tableId', stringifiedObject);<br>
   * </code>
   * 
   * @param tableId
   *          the id of the table you are adding to
   * @param valuesMap
   *          a stringified JSON object mapping element key to value. The values
   *          must be able to be parsed to the appropriate types for each row.
   *          E.g. an integer column cannot have a value "cat".
   * @return If the add was successful, return the id of the added row. Null if
   *         the add failed.
   */
  @android.webkit.JavascriptInterface
  public String addRow(String tableId, String stringifiedObject) {
    try {
      return weakControl.get().addRow(tableId, stringifiedObject);
    } catch (RemoteException e) {
      String appName = weakControl.get().retrieveAppName();
      WebLogger.getLogger(appName).printStackTrace(e);
      WebLogger.getLogger(appName).e(TAG, "Error accessing database: " + e.toString());
      Toast.makeText(weakControl.get().mActivity, R.string.error_accessing_database, Toast.LENGTH_LONG).show();
      return null;
    }
  }

  /**
   * CHANGE TO ASYNC
   * CHANGE TO ASYNC
   * CHANGE TO ASYNC
   * CHANGE TO ASYNC
   * CHANGE TO ASYNC
   * CHANGE TO ASYNC
   *
   * Update the row specified by rowId. Fails if the table cannot be found, if a
   * column in the stringifiedObject cannot be found, or if a value cannot be
   * parsed to the correct type.
   * <p>
   * For more detailed usage, refer to the documentation in
   * {@link #addRow(String, String)}.
   * 
   * @param tableId
   * @param stringifiedObject
   * @param rowId
   * @return
   * @see ControlIf#addRow(String, String)
   */
  @android.webkit.JavascriptInterface
  public boolean updateRow(String tableId, String stringifiedObject, String rowId) {
    try {
      return weakControl.get().updateRow(tableId, stringifiedObject, rowId);
    } catch (RemoteException e) {
      String appName = weakControl.get().retrieveAppName();
      WebLogger.getLogger(appName).printStackTrace(e);
      WebLogger.getLogger(appName).e(TAG, "Error accessing database: " + e.toString());
      Toast.makeText(weakControl.get().mActivity, R.string.error_accessing_database, Toast.LENGTH_LONG).show();
      return false;
    }
  }

  /**
   * Determine if the column exist in the given table.
   * 
   * @param tableId
   * @param elementPath
   * @return true if the column exists, else false. Returns false also if the
   *         tableId does not match any table.
   */
  @android.webkit.JavascriptInterface
  public boolean columnExists(String tableId, String elementPath) {
    try {
      return weakControl.get().columnExists(tableId, elementPath);
    } catch (RemoteException e) {
      String appName = weakControl.get().retrieveAppName();
      WebLogger.getLogger(appName).printStackTrace(e);
      WebLogger.getLogger(appName).e(TAG, "Error accessing database: " + e.toString());
      Toast.makeText(weakControl.get().mActivity, R.string.error_accessing_database, Toast.LENGTH_LONG).show();
      return false;
    }
  }

  /**
   * Take the path of a file relative to the app folder and return a url by
   * which it can be accessed.
   * 
   * @param relativePath
   * @return an absolute URI to the file
   */
  @android.webkit.JavascriptInterface
  public String getFileAsUrl(String relativePath) {
    return weakControl.get().getFileAsUrl(relativePath);
  }

  /**
   * Convert the rowpath value for a media attachment (e.g., uriFragment) field
   * into a url by which it can be accessed.
   * 
   * @param tableId
   * @param rowId
   * @param rowPathUri
   * @return
   */
  @android.webkit.JavascriptInterface
  public String getRowFileAsUrl(String tableId, String rowId, String rowPathUri) {
    return weakControl.get().getRowFileAsUrl(tableId, rowId, rowPathUri);
  }

  /**
   * Return the platform info as a stringified json object. This is an object
   * containing the keys: container, version, appName, baseUri, logLevel.
   * 
   * @return a stringified json object with the above keys
   */
  @android.webkit.JavascriptInterface
  public String getPlatformInfo() {
    return weakControl.get().getPlatformInfo();
  }

}