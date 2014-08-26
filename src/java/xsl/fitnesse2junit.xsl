<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- Written by James Zhang -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
 <xsl:element name="testsuite">
   <xsl:attribute name="tests">
     <!-- if a suite count tests else treat test as a 1-test suite -->
     <xsl:choose>
       <xsl:when test="suiteResults">
         <xsl:value-of select="sum(suiteResults/finalCounts/*)" />
       </xsl:when>
       <xsl:otherwise>
         <xsl:text>1</xsl:text>
       </xsl:otherwise>
     </xsl:choose>
   </xsl:attribute>
   <xsl:attribute name="failures">
     <!-- if a suite count wrong else see if test counts as wrong -->
     <xsl:choose>
       <xsl:when test="suiteResults">
         <xsl:value-of select="suiteResults/finalCounts/wrong" />
       </xsl:when>
       <xsl:otherwise>
         <!-- if test has any wrongs it counts as wrong -->
         <xsl:choose>
	       <xsl:when test="testResults/result/counts/wrong > 0">
	         <xsl:text>1</xsl:text>
	       </xsl:when>
	       <xsl:otherwise>
	         <xsl:text>0</xsl:text>
	       </xsl:otherwise>
	     </xsl:choose>
       </xsl:otherwise>
     </xsl:choose>
   </xsl:attribute>
   <xsl:attribute name="disabled">
     <!-- if a suite count ignored else a single test is never ignored -->
     <xsl:choose>
       <xsl:when test="suiteResults">
         <xsl:value-of select="suiteResults/finalCounts/ignores" />
       </xsl:when>
       <xsl:otherwise>
	     <xsl:text>0</xsl:text>
	   </xsl:otherwise>
     </xsl:choose>
   </xsl:attribute>
   <xsl:attribute name="errors">
     <!-- if a suite count exceptions else see if test counts as exception -->
     <xsl:choose>
       <xsl:when test="suiteResults">
         <xsl:value-of select="suiteResults/finalCounts/exceptions" />
       </xsl:when>
       <xsl:otherwise>
         <!-- if test has any wrongs it was wrong else check for exceptions -->
         <xsl:choose>
	       <xsl:when test="testResults/result/counts/wrong > 0">
	         <xsl:text>0</xsl:text>
	       </xsl:when>
	       <xsl:otherwise>
	         <!-- if test has any exceptions it counts as exception -->
	         <xsl:choose>
	           <xsl:when test="testResults/result/counts/exceptions > 0">
	             <xsl:text>1</xsl:text>
	           </xsl:when>
	           <xsl:otherwise>
			     <xsl:text>0</xsl:text>
			   </xsl:otherwise>
	         </xsl:choose>
	       </xsl:otherwise>
	     </xsl:choose>
       </xsl:otherwise>
     </xsl:choose>
   </xsl:attribute>
   <xsl:attribute name="name">
     <!-- use root path either way -->
     <xsl:choose>
       <xsl:when test="suiteResults">
         <xsl:value-of select="/suiteResults/rootPath" />
       </xsl:when>
       <xsl:otherwise>
         <xsl:value-of select="/testResults/rootPath" />
       </xsl:otherwise>
     </xsl:choose>
   </xsl:attribute>
   <!-- if a suite go through tests for info else get single test info -->
  <xsl:choose>
  <xsl:when test="suiteResults">
<xsl:for-each select="suiteResults/pageHistoryReference">
  <xsl:element name="testcase">
    <xsl:attribute name="classname">
      <xsl:value-of select="/suiteResults/rootPath" />
    </xsl:attribute>
    <xsl:attribute name="name">
      <xsl:value-of select="name" />
    </xsl:attribute>
    <xsl:attribute name="time">
      <xsl:value-of select="runTimeInMillis div 1000" />
    </xsl:attribute>
    <xsl:choose>
      <xsl:when test="counts/exceptions > 0">
        <xsl:element name="error">
          <xsl:attribute name="message">
            <xsl:value-of select="counts/exceptions" />
            <xsl:text> exceptions thrown</xsl:text>
            <xsl:if test="counts/wrong > 0">
              <xsl:text> and </xsl:text>
              <xsl:value-of select="counts/wrong" />
              <xsl:text> assertions failed</xsl:text>
            </xsl:if>
          </xsl:attribute>
        </xsl:element>
      </xsl:when>
      <xsl:when test="counts/wrong > 0">
        <xsl:element name="failure">
          <xsl:attribute name="message">
            <xsl:value-of select="counts/wrong" />
            <xsl:text> assertions failed</xsl:text>
          </xsl:attribute>
        </xsl:element>
      </xsl:when>
    </xsl:choose>
  </xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="testcase">
    <xsl:attribute name="classname">
      <xsl:value-of select="/testResults/rootPath" />
    </xsl:attribute>
    <xsl:attribute name="name">
      <xsl:value-of select="/testResults/rootPath" />
    </xsl:attribute>
    <xsl:attribute name="time">
      <xsl:value-of select="/testResults/result/runTimeInMillis div 1000" />
    </xsl:attribute>
    <xsl:choose>
      <xsl:when test="testResults/result/counts/exceptions > 0">
        <xsl:element name="error">
          <xsl:attribute name="message">
            <xsl:value-of select="testResults/result/counts/exceptions" />
            <xsl:text> exceptions thrown</xsl:text>
            <xsl:if test="testResults/result/counts/wrong > 0">
              <xsl:text> and </xsl:text>
              <xsl:value-of select="testResults/result/counts/wrong" />
              <xsl:text> assertions failed</xsl:text>
            </xsl:if>
          </xsl:attribute>
        </xsl:element>
      </xsl:when>
      <xsl:when test="testResults/result/counts/wrong > 0">
        <xsl:element name="failure">
          <xsl:attribute name="message">
            <xsl:value-of select="testResults/result/counts/wrong" />
            <xsl:text> assertions failed</xsl:text>
          </xsl:attribute>
        </xsl:element>
      </xsl:when>
    </xsl:choose>
  </xsl:element>
</xsl:otherwise>
  </xsl:choose>
 </xsl:element>
</xsl:template>
</xsl:stylesheet>
