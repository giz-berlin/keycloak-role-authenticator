<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "header">
        ${msg("accessDenied")}
    <#elseif section = "form">
        <div id="kc-error-message">
            <#if description?has_content>
                <p class="instruction">${description}</p>
            <#else>
                <p class="instruction">${msg("accessDeniedDescription")}</p>
            </#if>
        </div>
    </#if>
</@layout.registrationLayout>
