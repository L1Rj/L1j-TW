<%@ Control Language="VB" AutoEventWireup="false" CodeFile="ServerStatus.ascx.vb" Inherits="UserControl_Server_ServerStatus" %>

<table border="0" cellpadding="0" cellspacing="0" summary="排版表格">
    <tr>
        <td>
            目前各伺服器的狀態
        </td>
    </tr>
    <tr>
        <td>
            <table border="0" cellpadding="0" cellspacing="0" width="500px">
                <tr>
                    <td align="right">
                        <asp:Image ID="Image2" runat="server" ImageUrl="~/Images/ServerStatus/start.gif" />
                    </td>
                    <td>伺服器正常</td>
                    <td align="right">
                        <asp:Image ID="Image3" runat="server" ImageUrl="~/Images/ServerStatus/error.gif" />
                    </td>
                    <td>伺服器異常</td>
                    <td align="right">
                        <asp:Image ID="Image4" runat="server" ImageUrl="~/Images/ServerStatus/stop.gif" />
                    </td>
                    <td>伺服器維護</td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>
            <table border="1" cellpadding="0" cellspacing="0" width="500px">
                <tr>
                    <td style="width:33%">狀態</td>
                    <td style="width:67%">伺服器名稱</td>
                </tr>
                <tr>
                    <td><asp:Label ID="lblL1JTWName" runat="server"></asp:Label></td>
                    <td><asp:Image ID="imgStatus" runat="server" /></td>
                </tr>
            </table>
        </td>
    </tr>
</table>