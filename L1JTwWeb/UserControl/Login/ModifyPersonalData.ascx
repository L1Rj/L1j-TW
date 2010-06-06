<%@ Control Language="VB" AutoEventWireup="false" CodeFile="ModifyPersonalData.ascx.vb" Inherits="UserControl_Login_ModifyPersonalData" %>

<script language="javascript" type="text/javascript">
    function Clear() {
        var OldPwd = document.getElementById("<%=txtOldPwd.ClientID %>")
        var NewPwd = document.getElementById("<%=txtNewPwd.ClientID %>")
        var CNewPwd = document.getElementById("<%=txtCNewPwd.ClientID %>")

        OldPwd.value = "";
        NewPwd.value = "";
        CNewPwd.value = "";

        return false;
    }
</script>

<asp:Panel ID="pnlModify" runat="server">
    <table border="0" cellpadding="3" cellspacing="0" summary="排版表格" style="border-style: solid; border-width: 1px; border-color: #6699FF">
        <tr>
            <td align="right">帳號：</td>
            <td align="left"><asp:Label ID="lblAccount" runat="server"></asp:Label></td>
        </tr>
        <tr>
            <td align="right">舊密碼：</td>
            <td>
                <asp:TextBox ID="txtOldPwd" runat="server" MaxLength="16" Columns="16" 
                    ValidationGroup="vgModify" TextMode="Password"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td align="right">新密碼：</td>
            <td>
                <asp:TextBox ID="txtNewPwd" runat="server" MaxLength="16" Columns="16" 
                    ValidationGroup="vgModify" TextMode="Password"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td align="right">確認密碼</td>
            <td>
                <asp:TextBox ID="txtCNewPwd" runat="server" MaxLength="16" Columns="16" 
                    ValidationGroup="vgModify" TextMode="Password"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td align="right">E-Mail</td>
            <td>
                <asp:TextBox ID="txtEMail" runat="server" MaxLength="50"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                <asp:CompareValidator ID="cvPassword" runat="server" 
                    ErrorMessage="密碼和確認密碼必須相符。<br />" ControlToCompare="txtNewPwd" 
                    ControlToValidate="txtCNewPwd" Display="Dynamic" 
                    ValidationGroup="vgModify"></asp:CompareValidator>
                <asp:Label ID="lblMsg" runat="server" Visible="False" />
            </td>
        </tr>
        <tr>
            <td colspan="2" align="right">
                <asp:Button ID="btnModify" runat="server" BackColor="White" 
                    BorderColor="#507CD1" BorderStyle="Solid" BorderWidth="1px" CommandName="Login" 
                    Font-Names="Verdana" ForeColor="#284E98" Text="修改" 
                    ValidationGroup="vgModify" />
                &nbsp;<asp:Button ID="btnClear" runat="server" BackColor="#FFFBFF"
                    BorderColor="#507CD1" BorderStyle="Solid" BorderWidth="1px" 
                    Font-Names="Verdana" ForeColor="#284E98" Text="清除" OnClientClick="return Clear();" />
            </td>
        </tr>
    </table>
</asp:Panel>