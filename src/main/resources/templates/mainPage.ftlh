<!DOCTYPE html>
<html lang="">
<head>
    <title>TSP Solver</title>
    <link href="/css/mainPaigeStyle.css" rel="stylesheet">
    <link rel="icon" type="image/png" href="/images/faviconGPT.ico">
</head>
<body>
<div class="div1">
    <div class="div2">
        <h2 class="div2Text">Model</h2>
        <svg class="div1" id="cityMap">
            <#if changedRoute?has_content>
                <g class="lines">
                    <#list cities as city1>
                        <#list cities as city2>
                            <#if city1 != city2>
                                <line class="line1" x1="${city1.getxCoordinate()}" y1="${city1.getyCoordinate()}"
                                      x2="${city2.getxCoordinate()}" y2="${city2.getyCoordinate()}"/>
                            </#if>
                        </#list>
                    </#list>
                    <#list changedRoute as currentCity>
                        <#if currentCity?has_next && currentCity?has_content>
                            <#assign nextCity = changedRoute[currentCity?index + 1]>
                            <line class="line2" x1="${currentCity.getxCoordinate()}" y1="${currentCity.getyCoordinate()}"
                                  x2="${nextCity.getxCoordinate()}" y2="${nextCity.getyCoordinate()}"/>
                        </#if>
                    </#list>
                </g>
                <g class="numbers">
                    <#list cities as city>
                        <circle cx="${city.getxCoordinate()}" cy="${city.getyCoordinate()}" r="15" fill="white" stroke="#27ae60" stroke-width="3"/>
                        <text class="number" x="${city.getxCoordinate()}" y="${city.getyCoordinate() + 6}" fill="black" text-anchor="middle" alignment-baseline="middle">${city.getCityIndex()}</text>
                        <#if useRestrictions = true>
                            <text class="number" x="${city.getxCoordinate() + 20}" y="${city.getyCoordinate() + 20}"
                                  fill="red">${restrictions[city.getCityIndex() - 1]}</text>
                        </#if>
                    </#list>
                </g>
            <#else>
                <g class="lines">
                    <#list cities as city1>
                        <#list cities as city2>
                            <#if city1 != city2>
                                <line class="line1" x1="${city1.getxCoordinate()}" y1="${city1.getyCoordinate()}"
                                      x2="${city2.getxCoordinate()}" y2="${city2.getyCoordinate()}"/>
                            </#if>
                        </#list>
                    </#list>
                    <#list route as currentCity>
                        <#if currentCity?has_next && currentCity?has_content>
                            <#assign nextCity = route[currentCity?index + 1]>
                            <line class="line2" x1="${currentCity.getxCoordinate()}" y1="${currentCity.getyCoordinate()}"
                                  x2="${nextCity.getxCoordinate()}" y2="${nextCity.getyCoordinate()}"/>
                        </#if>
                    </#list>
                </g>
                <g class="numbers">
                    <#list cities as city>
                        <circle cx="${city.getxCoordinate()}" cy="${city.getyCoordinate()}" r="15" fill="white" stroke="#27ae60" stroke-width="3"/>
                        <text class="number" x="${city.getxCoordinate()}" y="${city.getyCoordinate() + 6}" fill="black" text-anchor="middle" alignment-baseline="middle">${city.getCityIndex()}</text>
                        <#if useRestrictions = true>
                            <text class="number" x="${city.getxCoordinate() + 20}" y="${city.getyCoordinate() + 20}" fill="red">${restrictions[city.getCityIndex() - 1]}</text>
                        </#if>
                    </#list>
                </g>
            </#if>
        </svg>
    </div>
    <div>
        <div class="div3">
            <div class="summaryAndButtons">
                <section class="summary">
                    <h1>Summary</h1>
                    <text class="textDisplayed">Method: </text><text class="outputText">${method}</text><br>
                    <text class="textDisplayed">Elapsed time: </text><text class="outputText">${time}</text><br>
                    <text class="textDisplayed">Points: </text><text class="outputText">${numCities}</text><br>
                    <text class="textDisplayed">Start-End point: </text><text class="outputText">${startCity}</text><br>
                    <text class="textDisplayed">Travelers: </text><text class="outputText">${numTraveler}</text><br>
                    <text class="textDisplayed">Load capacity: </text><text class="outputText">${loadCapacity}</text><br>
                </section>
                <section class="mainButtons">
                    <div>
                        <button class="submit-button" id="openModalConfigTSP" onclick="insertDataToTspConfigPage()">Set TSP</button>
                        <#include "configTspPage.html">
                        <script src="/js/openCloseHandlerConfigTspPaige.js"></script>
                    </div>
                    <form method="POST" action="/GENERATE">
                        <button class="submit-button" >Generate</button>
                    </form>
                    <div>
                        <button id="openModalConfigGA" onclick="insertDataToGaConfigPage()" class="submit-button">GA config</button>
                        <#include "configGaPage.html">
                        <script src="/js/openCloseHandlerConfigGaPaige.js"></script>
                    </div>
                    <form id="fileUploadForm" method="post" enctype="multipart/form-data">
                        <label class="file-label" for="fileInput">Select File</label>
                        <input type="file" name="file" id="fileInput" class="file-input" accept=".tsp"><br>
                        <button type="submit" id="submitButton" class="submit-button" disabled>Import</button>
                    </form>
                </section>
            </div>
            <section class="setMethod">
                <label for="select"></label><select name="selectedMethod" id="select">
                    <option value="GA">Genetic algorithm</option>
                    <option value="B&B">Branch and Bound</option>
                </select>
                <button class="submit-button" id="solveButton" onclick="sendDataFromMainPaige()">Solve</button>
                <text id="errorText"></text>
            </section>
            <section class="pathInfo">
                <text class="textDisplayed">Optimal path:</text><br>
                <section id="pathSection">
                    <#if route?has_content>
                        <#assign first=route[0]>
                        <#list route as currentCity>
                            <#if currentCity?has_next && currentCity?has_content>
                                <#if currentCity = first && !currentCity?is_first>
                                    <text>${currentCity.cityIndex}</text><br>
                                    <text>${first.cityIndex}&rarr;</text>
                                <#else>
                                    <text>${currentCity.cityIndex}&rarr;</text>
                                </#if>
                            <#else>
                                <text>${currentCity.cityIndex}</text>
                            </#if>
                        </#list>
                    </#if>
                </section>
                <text class="textDisplayed">Route cost: </text><text class="outputText">${cost}</text><br>
                <text class="textDisplayed">Changed path:</text><br>
                <section id="changedPathSection">
                    <#if changedRoute?has_content>
                        <#assign first=route[0]>
                        <#list changedRoute as currentCity>
                            <#if currentCity?has_next && currentCity?has_content>
                                <#if currentCity = first && !currentCity?is_first>
                                    <text>${currentCity.cityIndex}</text><br>
                                    <text>${first.cityIndex}&rarr;</text>
                                <#else>
                                    <text>${currentCity.cityIndex}&rarr;</text>
                                </#if>
                            <#else>
                                <text>${currentCity.cityIndex}</text>
                            </#if>
                        </#list>
                    </#if>
                </section>
                <text class="textDisplayed">Changed Route cost: </text><text class="outputText">${newCost}</text><br>
                <#if useRestrictions && changedRoute?has_content>
                    <text class="textDisplayed">Restrictions: </text><text class="outputText">${isRes?string("yes", "no")}</text><br>
                </#if>
            </section>
            <section class="export">
                <div>
                    <button class="submit-button" id="openModalChangePath" onclick="insertDataToChangePaige()"
                            <#if !route?has_content>disabled</#if>>Change path</button>
                    <#include "changePathPaige.html">
                    <script src="/js/openCloseHandlerChangePathPaige.js"></script>
                </div>
                <button  class="submit-button" id="download" <#if cost == 0>disabled</#if>>Export result</button>
            </section>
        </div>
        <div class="div4">
            <text>TSP Solver, select file or generate random task</text>
        </div>
    </div>
</div>
</body>
</html>
<script src="/js/insertDataToGaConfigPage.js"></script>
<script src="/js/insertDataToTspConfigPage.js"></script>
<script src="/js/insertDataToChangePathPage.js"></script>
<script src="/js/insertDataToMainPage.js"></script>
<script src="/js/uploadFile.js"></script>
<script src="/js/activeInActiveForMainPaige.js"></script>
<script src="/js/downloadReport.js"></script>
<script src="/js/saveButtonHandlerMainPaige.js"></script>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>