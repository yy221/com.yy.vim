//chliu created 2013/6/18 14:46:27
//
package com.yy.vim.handlers;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class MyHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public MyHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		int lineNumber = 0;
		// IEditorPart editor2 =  HandlerUtil.getActiveEditor(event);
		
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();
		IEditorPart editor = page.getActiveEditor();
		
		//get line number
		if (editor instanceof ITextEditor) 
		{
		  ISelectionProvider selectionProvider = ((ITextEditor)editor).getSelectionProvider();
		  ISelection selection = selectionProvider.getSelection();
		  if (selection instanceof ITextSelection) 
		  {
		    ITextSelection textSelection = (ITextSelection)selection;
		    lineNumber = textSelection.getStartLine()+1; // etc.
		  }
		}
		
		//get file path, ${selected_resource_loc}
		IEditorInput input = editor.getEditorInput();
		IPath path = ( input instanceof FileEditorInput ) ? ( (FileEditorInput)input).getPath() : null;
		if (path != null)
		{
		    // System.out.println ( "IPath is:" + path.toOSString() );
		}
				    		
		//get project path,${project_loc}
		// call gvim.exe here now!
		File f = new File ("C:\\Program Files (x86)\\Vim\\vim73\\gvim.exe");
		String vimpath = null;
		if (f.exists()) 
		{
		    vimpath = "\"C:\\Program Files (x86)\\Vim\\vim73\\gvim.exe\"";  
		}
		else
		{
		    vimpath = "\"C:\\Program Files\\Vim\\vim73\\gvim.exe\"";  
		}
		String cmd = String.format( "%s --servername eclipse --remote-silent +%d \"%s\"", 
                vimpath, lineNumber, path.toOSString()  ); 
		
		//System.out.println( "start vim: "+ cmd );
		
	    try {
	    	//TODO: change working dir to project dir !  
	    	ProcessBuilder build = new ProcessBuilder(cmd);

            if (true)
            {
                //get object which represents the workspace  
                IWorkspace workspace = ResourcesPlugin.getWorkspace();  		
                //get location of workspace (java.io.File)  
                IResource res = workspace.getRoot();		
                IProject prj = res.getProject();
                
                // build.directory ( prj.getFullPath().toFile() );
                
                //System.out.println( "workspace path: "  );		
                //System.out.println( res.getLocation().toOSString() ); 
                build.directory ( res.getLocation().toFile()  );
            }
            else
            {
//                if (win != null)
//                {
//                    IStructuredSelection selection = (IStructuredSelection) win.getSelectionService().getSelection();
//                    Object firstElement = selection.getFirstElement();
//                    if (firstElement instanceof IAdaptable)
//                    {
//                        IProject project = (IProject)((IAdaptable)firstElement).getAdapter(IProject.class); 
//                        build.directory (project.getFullPath().toFile());
//                    }
//                }
            }

			Process p = build.start();

			//p.wait();
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
