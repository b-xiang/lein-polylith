
<#include "macros.ftl">
<#include "macros.ftl">

<!DOCTYPE html>
<html>
<head>
<title>${workspace.name} (workspace)</title>

<link rel="stylesheet" type="text/css" href="style.css">

</head>
<body>

<script>
function selected(base, selectedMenu, selectedValue) {
  var toggle = document.getElementById(selectedMenu).className == selectedValue;
  var expanded = document.getElementById(base).value == "expanded";
  var toggledExpanded = toggle ? !expanded : expanded;
  var viewExpanded = toggledExpanded ? "block" : "none";
  var viewCollapsed = toggledExpanded ? "none" : "block";
  document.getElementById(base).value = (toggledExpanded ? "expanded" : "collapsed");

  return {expanded: viewExpanded,
          collapsed: viewCollapsed};
}

<#list bases as base>
  <#list base.environments as env>

function ${env.id}() {
    var table = selected("${base.name}", "${env.id}", "${env.type}-on");

    document.getElementById("${env.id}").className = "${env.type}-on";
    <#if env.type == "system" || env.type == "environment">
    document.getElementById("${env.id}__expanded").style.display = table.expanded;
    document.getElementById("${env.id}__collapsed").style.display = table.collapsed;
    <#else>
    document.getElementById("${env.id}__").style.display = "block";
    </#if>
    <#list base.environments as env2>
      <#if env.name != env2.name || env.type != env2.type>
    document.getElementById("${env2.id}").className = "${env2.type}-off";
    <#if env2.type == "system" || env2.type == "environment">
    document.getElementById("${env2.id}__expanded").style.display = "none";
    document.getElementById("${env2.id}__collapsed").style.display = "none";
    <#else>
    document.getElementById("${env2.id}__").style.display = "none";
    </#if>
      </#if>
    </#list>
}
  </#list>
</#list>
</script>

<#list bases as base>
<input type="hidden" id="${base.name}" name="${base.name}" value="collapsed">
</#list>

<img src="../logo.png" alt="Polylith" style="width:200px;">

<p class="clear"/>
<@doc dir = "" entity = workspace size=32/>

<h1>Libraries</h1>
<table class="entity-table">
  <tr>
  <#if githubUrl != "">
    <td class="github-header"/>
  </#if>
    <td/>
<#list libraries as lib>
    <td class="library-header"><span class="vertical-text">${dashify(lib.name)}&nbsp;&nbsp;${lib.version}</div></td>
</#list>
  </tr>
<@libRows entities=components type="component"/>
<@libRows entities=bases type="base"/>
<@libRows entities=environments type="environment"/>
<@libRows entities=systems type="system"/>
</table>

<h1>Building blocks</h1>

<table class="entity-table">
  <tr>
    <td></td>
<#list environments as env>
    <td class="environment-header" title="${env.description}"><span class="vertical-text">${env.name}</span></td>
</#list>
<#list systems as sys>
    <td class="system-header" title="${sys.description}"><span class="vertical-text">${sys.name}</span></td>
</#list>
  </tr>
<@entityRows entities=components type="component"/>
<@entityRows entities=bases type="base"/>
</table>

<#--
<h3>Systems</h3>
<div class="systems">
<#list systems as system>
  <@doc dir = "systems" entity = system/>

  <p class="clear"/>
  <a nohref id="${system.name}-ref" style="cursor:pointer;color:blue;margin-left:10px;" onClick="toggleTableSize('${system.name}')">>-<</a>
  <p class="tiny-clear"/>

  <#list system.unreferencedComponents as entity>
    <#if entity.name = "&nbsp;">
    <@component c=entity title="The interface '${entity.name}' is referenced from '${system.name}' but a component that implements the '${entity.name}' interface also needs to be added to ${system.name}', otherwise it will not compile."/>
    <#else>
    <@component c=entity title="The component '${entity.name}' was added to '${system.name}' but has no references to it in the source code."/>
    </#if>
  </#list>
  <#if system.entities?has_content>
  <p class="tiny-clear"/>
  </#if>
  <@table name=system.name table=system.smalltable size="small"/>
  <@table name=system.name table=system.mediumtable size="medium"/>
</#list>
</div>
-->

<#--
<h2>Interfaces</h1>
<#list interfaces as interface>
  <a id="${interface}-interface"/>
  <h3>${interface}</h3>
  <div class="interface">${interface}</div>
  <p class="clear"/>
</#list>
-->

<#--
<h2>Components</h2>
<#list components as component>
  <a id="${component.name}-component"/>
  <@doc dir = "components" entity = component/>
  <@table name=component.name table=component.tables.pure/>
  <p class="tiny-clear"/>
</#list>
-->

<h1>Bases</h1>
<#list bases as base>
  <a id="${base.name}-base"/>
  <@doc dir = "bases" entity = base/>
  <p class="tiny-clear"/>
  <#list base.environments as env>
  <#assign state><#if env.type != "environment" && env.type != "system">-on<#else>-off</#if></#assign>
  <div id="${env.id}" class="${env.type}${state}" onclick="${env.id}();">${env.name}</div>
  </#list>
  <p class="tiny-clear"/>
  <#list base.tableDefs as def>
  <#assign show = (def.info.type != "environment" && def.info.type != "system")/>
  <@table name=base.name table=def.table id=def.info.id selected=show/>
  </#list>
  <p class="tiny-clear"/>
</#list>

</body>
</html>
