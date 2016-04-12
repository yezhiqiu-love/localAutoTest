<!--/*--><root><![CDATA[<!--*/-->
function isIE()
{
    return /msie/i.test(navigator.userAgent) && !/opera/i.test(navigator.userAgent);
}

function correctAllCollapseImages()
{
  divs = document.getElementsByTagName("div");
  for (i = 0; i < divs.length; i++)
  {
    div = divs[i];
    if (div.className == collapsableOpenCss || div.className == collapsableClosedCss)
    {
      toggleCollapsable(div.id);
      toggleCollapsable(div.id);
    }
  }
}
<!--/*-->]]></root><!--*/-->

