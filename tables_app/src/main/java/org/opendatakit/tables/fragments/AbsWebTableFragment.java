/*
 * Copyright (C) 2014 University of Washington
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
package org.opendatakit.tables.fragments;

import java.lang.ref.WeakReference;

import org.opendatakit.common.android.application.CommonApplication;
import org.opendatakit.common.android.listener.DatabaseConnectionListener;
import org.opendatakit.common.android.utilities.WebLogger;
import org.opendatakit.database.service.OdkDbInterface;
import org.opendatakit.tables.R;
import org.opendatakit.tables.activities.AbsBaseActivity;
import org.opendatakit.tables.application.Tables;
import org.opendatakit.tables.utils.Constants;
import org.opendatakit.tables.utils.IntentUtil;
import org.opendatakit.tables.utils.WebViewUtil;
import org.opendatakit.tables.views.webkits.*;

import android.app.Fragment;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * Base class for {@link Fragment}s that display information about a table
 * using a WebKit view.
 * @author sudar.sam@gmail.com
 *
 */
public abstract class AbsWebTableFragment extends AbsTableDisplayFragment
    implements IWebFragment, ICallbackFragment {

  private static final String TAG = AbsWebTableFragment.class.getSimpleName();
  private static final String RESPONSE_JSON = "responseJSON";
  /**
   * The {@link Control} object that was used to generate the
   * {@link ControlIf} that was passed to the {@link WebView}. This reference
   * must be saved to prevent garbage collection of the {@link WeakReference}
   * in {@link ControlIf}.
   */
  Control mControlReference;
  Data mDataReference;
  String responseJSON = null;

  DatabaseConnectionListener listener = null;
  /**
   * The {@link TableData} object that was used to generate the
   * {@link TableDataIf} that was passed to the {@link WebView}. This reference
   * must be saved to prevent garbage collection of the {@link WeakReference}
   * in {@link TableDataIf}.
   */
  TableData mTableDataReference;

  /** The file name this fragment is displaying. */
  String mFileName;
  
  /**
   * Retrieve the file name that should be displayed.
   * @return the file name, or null if one has not been set.
   */
  @Override
  public String retrieveFileNameFromBundle(Bundle bundle) {
    String fileName = IntentUtil.retrieveFileNameFromBundle(bundle);
    return fileName;
  }

  @Override
  public void putFileNameInBundle(Bundle bundle) {
    if (this.getFileName() != null) {
      bundle.putString(Constants.IntentKeys.FILE_NAME, this.getFileName());
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // AppName is unknown since Activity is likely null
    // Get the file name if it was there.
    String retrievedFileName = retrieveFileNameFromBundle(savedInstanceState);
    if (retrievedFileName == null) {
      // then try to get it from its arguments.
      retrievedFileName = this.retrieveFileNameFromBundle(this.getArguments());
    }
    this.mFileName = retrievedFileName;
    if ( savedInstanceState != null && savedInstanceState.containsKey(RESPONSE_JSON)) {
      responseJSON = savedInstanceState.getString(RESPONSE_JSON);
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    this.putFileNameInBundle(outState);
    if ( responseJSON != null ) {
      outState.putString(RESPONSE_JSON, responseJSON);
    }
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    WebLogger.getLogger(getAppName()).d(TAG, "[onCreateView]");
    
    ViewGroup v = (ViewGroup) inflater.inflate(
        R.layout.web_view_container,
        container,
        false);

    WebView webView = (WebView) v.findViewById(R.id.webkit);
    
    WebView result = WebViewUtil.getODKCompliantWebView((AbsBaseActivity) getActivity(), webView);
    return v;
  }
  
  /**
   * @throws RemoteException 
   * @see IWebFragment#createControlObject()
   */
  @Override
  public Control createControlObject() throws RemoteException {
    Control result = new Control((AbsBaseActivity) getActivity(), getTableId(), getColumnDefinitions());
    return result;
  }

  /**
   * Create a {@link TableData} object that can be added toe the webview.
   * @return
   * @throws RemoteException
   */
  protected abstract TableData createDataObject() throws RemoteException;

  public Data getDataReference() throws RemoteException {
    if ( mDataReference == null ) {
      mDataReference = new Data(this);
    }
    return mDataReference;
  }

  @Override
  public void setWebKitVisibility() {
    if ( getView() == null ) {
      return;
    }
    
    WebView webView = (WebView) getView().findViewById(R.id.webkit);
    TextView noDatabase = (TextView) getView().findViewById(android.R.id.empty);
    
    if ( Tables.getInstance().getDatabase() != null ) {
      webView.setVisibility(View.VISIBLE);
      noDatabase.setVisibility(View.GONE);
    } else {
      webView.setVisibility(View.GONE);
      noDatabase.setVisibility(View.VISIBLE);
    }
  }

  /**
   * Get the file name this fragment is displaying.
   */
  @Override
  public String getFileName() {
    return this.mFileName;
  }

  @Override
  public void setFileName(String relativeFileName) {
    this.mFileName = relativeFileName;
    databaseAvailable();
  }


  @Override
  public void databaseAvailable() {
    if ( listener != null ) {
      listener.databaseAvailable();
    }
  }

  @Override
  public void databaseUnavailable() {
    if ( listener != null ) {
      listener.databaseUnavailable();
    }
  }

  @Override
  public void signalResponseAvailable(String responseJSON) {
    // TODO: this should be a queue of responses
    this.responseJSON = responseJSON;
    final WebView webView = (WebView) getView().findViewById(org.opendatakit.tables.R.id.webkit);
    this.getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        webView.loadUrl("javascript:datarsp.responseAvailable();");
      }
    });
  }

  @Override
  public String getResponseJSON() {
    // TODO: this should be a queue of responses
    String responseJSON = this.responseJSON;
    this.responseJSON = null;
    return responseJSON;
  }

  @Override
  public void registerDatabaseConnectionBackgroundListener(DatabaseConnectionListener listener) {
    this.listener = listener;
  }

  @Override
  public OdkDbInterface getDatabase() {
    return ((CommonApplication) this.getActivity().getApplication()).getDatabase();
  }
}
