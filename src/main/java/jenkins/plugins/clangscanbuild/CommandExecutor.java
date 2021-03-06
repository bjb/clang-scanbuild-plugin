/**
 * Copyright (c) 2011 Joshua Kennedy, http://deadmeta4.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jenkins.plugins.clangscanbuild;

import java.util.ArrayList;
import java.util.List;

import jenkins.plugins.clangscanbuild.commands.BuildContext;
import jenkins.plugins.clangscanbuild.commands.Command;

public class CommandExecutor {
	
	public static final int SUCCESS = 0;
	public static final int FAILURE = 1;
	
	private List<Command> commands = new ArrayList<Command>();
	
	private CommandExecutor( Command command ){
		commands.add( command );
	}
	
	public static CommandExecutor execute( Command command ){
		return new CommandExecutor( command );
	}
	
	public CommandExecutor and( Command command ){
		commands.add( command );
		return this;
	}
	
	public int withContext( BuildContext context ){
		for( Command command : commands ){
			try{
				return command.execute( context );
			}catch( Exception e ){
				context.log( "Error executing command: " + command + "\n" + e.getMessage() );
			}
		}
		return FAILURE;
	}
	
}
