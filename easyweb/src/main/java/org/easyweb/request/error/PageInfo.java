package org.easyweb.request.error;

/**
 * User: jimmey/shantong
 * Date: 13-7-4
 * Time: 下午10:42
 */
public class PageInfo {

    public static String code,code1;

    static {
        code = "<html>\n" +
                "<head>\n" +
                "    <style type=\"text/css\">\n" +
                "\n" +
                "        #error-info {\n" +
                "            TEXT-ALIGN: center;\n" +
                "            MARGIN-RIGHT: auto;\n" +
                "            MARGIN-LEFT: auto;\n" +
                "            width: 1200px;\n" +
                "        }\n" +
                "\n" +
                "        #error-info table.showdata th {\n" +
                "            background-color: #F9F9F9;\n" +
                "            border: 1px solid #FFFFFF;\n" +
                "            font-size: 12pt;\n" +
                "            padding: 4px;\n" +
                "        }\n" +
                "\n" +
                "        #error-info table.showdata {\n" +
                "            background-color: #CDCDCD;\n" +
                "            font-family: arial;\n" +
                "            font-size: 8pt;\n" +
                "            text-align: left;\n" +
                "        }\n" +
                "\n" +
                "        #error-info table.showdata thead tr th, #error-info table.showdata tfoot tr th {\n" +
                "            background-color: #F4F4F4;\n" +
                "            border: 1px solid #FFFFFF;\n" +
                "            font-size: 10pt;\n" +
                "            padding: 4px;\n" +
                "        }\n" +
                "\n" +
                "        #error-info table.showdata thead tr {\n" +
                "            background-position: right center;\n" +
                "            background-repeat: no-repeat;\n" +
                "            cursor: pointer;\n" +
                "        }\n" +
                "\n" +
                "        #error-info table.showdata tbody td {\n" +
                "            background-color: #FFFFFF;\n" +
                "            color: #3D3D3D;\n" +
                "            padding: 4px;\n" +
                "            vertical-align: top;\n" +
                "        }\n" +
                "\n" +
                "        #error-info table.showdata tbody tr.odd td {\n" +
                "            background-color: #F0F0F6;\n" +
                "        }\n" +
                "\n" +
                "        #error-info pre {\n" +
                "            white-space: pre-wrap; /* css-3 */\n" +
                "            white-space: -moz-pre-wrap; /* Mozilla, since 1999 */\n" +
                "            white-space: -pre-wrap; /* Opera 4-6 */\n" +
                "            white-space: -o-pre-wrap; /* Opera 7 */\n" +
                "            word-wrap: break-word; /* Internet Explorer 5.5+ */\n" +
                "            overflow-y: scroll;\n" +
                "        }\n" +
                "\n" +
                "        #error-info .hidden {\n" +
                "            display: none;\n" +
                "        }\n" +
                "\n" +
                "        #error-info ul, #error-info ol {\n" +
                "            list-style-type: none;\n" +
                "            list-style-position: inside;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div id=\"error-info\">\n" +
                "    <table width=\"1100\" class=\"showdata\" cellspacing=\"1\">\n" +
                "        <caption style='margin:8'><h1>Easyweb错误信息</h1></caption>\n" +
                "        <tr>\n" +
                "            <th width=\"100\">错误类型</th>\n" +
                "            <td width=\"1000\">\n" +
                "                $!errorInfo.errorType\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <th width=\"100\">错误类型信息</th>\n" +
                "            <td>\n" +
                "                $!errorInfo.typeMessage\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "\n" +
                "        <tr>\n" +
                "            <th width=\"100\">错误堆栈</th>\n" +
                "            <td>\n" +
                "                <pre>  $!errorInfo.exception</pre>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "\n" +
                "\n" +
                "        #if(\"$!errorInfo.errorPageException\" != \"\")\n" +
                "            <tr>\n" +
                "                <th width=\"100\">自定义错误堆栈</th>\n" +
                "                <td>\n" +
                "                    $!errorInfo.errorPageException\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "        #end\n" +
                "\n" +
                "        <tr>\n" +
                "            <td colspan=\"2\" align='center'>\n" +
                "                <ol style='list-style-type:none'>\n" +
                "                    #foreach($m in $messages)\n" +
                "                        <li> $ignoreTool.ignore($m)</li>\n" +
                "                    #end\n" +
                "                </ol>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "\n" +
                "    #if(!$errorInfo.appInfos.isEmpty())\n" +
                "        #foreach($info in $errorInfo.appInfos)\n" +
                "            <table width=\"1100\" class=\"showdata\" style=\"margin-top: 20px\" cellspacing=\"1\">\n" +
                "                <tr>\n" +
                "                    <th width=\"160\">应用名称</th>\n" +
                "                    <td width=\"160\">$info.app.name</td>\n" +
                "                    <th width=\"120\">版本号</th>\n" +
                "                    <td width=\"160\">$info.app.version </td>\n" +
                "                    <th width=\"120\">\n" +
                "                        应用状态\n" +
                "                    </th>\n" +
                "                    <td>$info.app.status</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <th >应用Groovy Bean</th>\n" +
                "                    <td colspan=\"5\">\n" +
                "                        #if($info.beans.isEmpty())\n" +
                "                            应用没有groovy bean\n" +
                "                        #else\n" +
                "                            <ul>\n" +
                "                                #foreach($b in $info.beans.keySet())\n" +
                "                                    <li>\n" +
                "                                        $b -> $info.beans.get($b)\n" +
                "                                    </li>\n" +
                "                                #end\n" +
                "                            </ul>\n" +
                "                        #end\n" +
                "\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <th >自定义VmTool</th>\n" +
                "                    <td colspan=\"5\">\n" +
                "                        #if($info.vmTools.isEmpty())\n" +
                "                            应用没有自定义Vm工具\n" +
                "                        #else\n" +
                "                            <ul>\n" +
                "                                #foreach($b in $info.vmTools.keySet())\n" +
                "                                    <li>\n" +
                "                                        $b -> $info.vmTools.get($b)\n" +
                "                                    </li>\n" +
                "                                #end\n" +
                "                            </ul>\n" +
                "                        #end\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <th>注册的页面</th>\n" +
                "                    <td colspan=\"5\">\n" +
                "                        #if($info.appUris.isEmpty())\n" +
                "                            应用没有自定义Vm工具\n" +
                "                        #else\n" +
                "                            <div style=\"width:900px;overflow: auto\"></div>\n" +
                "                            <ul>\n" +
                "                                #foreach($b in $info.appUris.keySet())\n" +
                "                                    <li>\n" +
                "                                        #set($pm = $info.appUris.get($b).pageMethod)\n" +
                "                                        $b -> $pm.method.name\n" +
                "                                    </li>\n" +
                "                                #end\n" +
                "                            </ul>\n" +
                "                        #end\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "            </table>\n" +
                "        #end\n" +
                "    #end\n" +
                "</div>\n" +
                "<script>\n" +
                "    function triggle(key) {\n" +
                "        var el = document.getElementById(key);\n" +
                "        if (hasClass(el, \"hidden\")) {\n" +
                "            removeClass(el, \"hidden\");\n" +
                "        }\n" +
                "        else {\n" +
                "            addClass(el, \"hidden\");\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    function hasClass(ele, cls) {\n" +
                "        return ele.className.match(new RegExp('(\\\\s|^)' + cls + '(\\\\s|$)'));\n" +
                "    }\n" +
                "\n" +
                "    function addClass(ele, cls) {\n" +
                "        if (!this.hasClass(ele, cls)) ele.className += \" \" + cls;\n" +
                "    }\n" +
                "\n" +
                "    function removeClass(ele, cls) {\n" +
                "        if (hasClass(ele, cls)) {\n" +
                "            var reg = new RegExp('(\\\\s|^)' + cls + '(\\\\s|$)');\n" +
                "            ele.className = ele.className.replace(reg, ' ');\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "</script>\n" +
                "</body>";

        code1="<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">\n" +
                "<html>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gbk\">\n" +
                "<title>系统异常</title>\n" +
                "<style type=\"text/css\">\n" +
                "    body {\n" +
                "        padding-top: 42px;\n" +
                "        margin: 0;\n" +
                "        font-family: \"Helvetica Neue\", Helvetica, Arial, sans-serif;\n" +
                "        font-size: 14px;\n" +
                "        line-height: 20px;\n" +
                "        color: #333333;\n" +
                "        height: 100%;\n" +
                "        overflow-y: scroll;\n" +
                "        background: #ffffff;\n" +
                "    }\n" +
                "\n" +
                "    div.dialog {\n" +
                "        /*background: transparent url(\"/ewassets/images/500.png\") no-repeat;*/\n" +
                "        height: 360px;\n" +
                "        margin: 50px auto;\n" +
                "        width: 400px;\n" +
                "    }\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"dialog\">\n" +
                "    系统错误，请稍后重试!!\n" +
                "\n" +
                "</div>\n" +
                "<div style=\"display: none\">\n" +
                "    <table width=\"1100\" class=\"showdata\" cellspacing=\"1\">\n" +
                "        <caption style='margin:8'><h1>Easyweb错误信息</h1></caption>\n" +
                "        <tr>\n" +
                "            <th width=\"100\">错误类型</th>\n" +
                "            <td width=\"1000\">\n" +
                "                $!errorInfo.errorType\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <th width=\"100\">错误类型信息</th>\n" +
                "            <td>\n" +
                "                $!errorInfo.typeMessage\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "\n" +
                "        <tr>\n" +
                "            <th width=\"100\">错误堆栈</th>\n" +
                "            <td>\n" +
                "                <pre>  $!errorInfo.exception</pre>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "\n" +
                "        #if(\"$!errorInfo.errorPageException\" != \"\")\n" +
                "            <tr>\n" +
                "                <th width=\"100\">自定义错误堆栈</th>\n" +
                "                <td>\n" +
                "                    $!errorInfo.errorPageException\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "        #end\n" +
                "    </table>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>\n";
    }

}
