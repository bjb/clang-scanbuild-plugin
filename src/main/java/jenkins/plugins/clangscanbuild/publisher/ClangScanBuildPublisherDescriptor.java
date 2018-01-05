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
package jenkins.plugins.clangscanbuild.publisher;

import jenkins.plugins.clangscanbuild.ClangScanBuildUtils;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;

import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import java.util.regex.Pattern;
import java.util.Set;
import java.util.HashSet;


public class ClangScanBuildPublisherDescriptor extends BuildStepDescriptor<Publisher>{

	public ClangScanBuildPublisherDescriptor(){
		super( ClangScanBuildPublisher.class );
		load();
	}

	@Override
	public Publisher newInstance(StaplerRequest arg0, JSONObject json ) throws hudson.model.Descriptor.FormException {

		boolean markBuildUnstable = false;
		int bugThreshold = 0;
		String excludedPaths = "";
		String reportFolderName = ClangScanBuildUtils.REPORT_OUTPUT_FOLDERNAME;
		String omitStrings = "";  // space-separated words, all one string
		Set<Pattern> omitPatterns = new HashSet<Pattern> ();

		JSONObject failWhenThresholdExceeded = json.optJSONObject( "failWhenThresholdExceeded" );
		if( failWhenThresholdExceeded != null ){
			markBuildUnstable = true;
			bugThreshold = failWhenThresholdExceeded.getInt( "bugThreshold" );
			excludedPaths = failWhenThresholdExceeded.getString( "clangexcludedpaths" );
		}

		reportFolderName = json.getString("reportFolderName");

		omitStrings = json.getString("omitStrs");
		if ((omitStrings == null) || (omitStrings == "")) {
		    omitStrings = ClangScanBuildUtils.OMIT_STRS;
		} else {
		    // split omitStrings into words, add parens around each,
		    // make them into regexes and add to omitPatterns
		    String omitArray[] = omitStrings.split("\\s+");
		    for (String os : omitArray) {
			omitPatterns.add(Pattern.compile("(" + os + ")"));
		    }
		}

		return new ClangScanBuildPublisher( markBuildUnstable, bugThreshold, excludedPaths, reportFolderName, omitStrings, omitPatterns );
	}

	@Override
	public String getDisplayName() {
		return "Publish Clang Scan-Build Results";
	}

	@Override
	public boolean isApplicable(Class<? extends AbstractProject> jobType){
		return AbstractProject.class.isAssignableFrom(jobType);
	}

}
