<%@ Control Language="VB" AutoEventWireup="false" CodeFile="Register.ascx.vb" Inherits="UserControl_Login_Register" %>

<script language="javascript" type="text/javascript">
    function Clear() {
        document.getElementById("<%=txtAccount.ClientID %>").value = "";
        document.getElementById("<%=txtPassword.ClientID %>").value = "";
        document.getElementById("<%=txtCPassword.ClientID %>").value = "";
        document.getElementById("<%=txtEmail.ClientID %>").value = "";
        document.getElementById("<%=txtQuestion.ClientID %>").value = "";
        document.getElementById("<%=txtAnswer.ClientID %>").value = "";
        document.getElementById("<%=txtCheckCode.ClientID %>").value = "";
        return false
    }
</script>

<asp:Panel ID="pnlCreateUsers" runat="server">
    <table border="0" cellpadding="3" cellspacing="5" summary="排版表格" style="border-style: solid; border-width: 1px; border-color: #6699FF">
        <tr>
            <td>
                <asp:Panel ID="pnlAccount" runat="server">
                    <table border="0" cellpadding="3" cellspacing="5" summary="排版表格">
                        <tr>
                            <td align="right">
                                <asp:Label ID="lblAccount" runat="server" Text="帳號："></asp:Label>
                            </td>
                            <td align="left">
                                <asp:TextBox ID="txtAccount" MaxLength="16" runat="server" Width="160px" 
                                    ValidationGroup="vgRegister"></asp:TextBox>
                                <asp:RequiredFieldValidator ID="rfvAccount" runat="server" ErrorMessage="*" 
                                    ControlToValidate="txtAccount" Display="Dynamic" 
                                    ValidationGroup="vgRegister" />
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
                                <asp:Label ID="lblPassword" runat="server" Text="密碼："></asp:Label>
                            </td>
                            <td align="left">
                                <asp:TextBox ID="txtPassword" MaxLength="16" runat="server" TextMode="Password" 
                                    Width="160px" ValidationGroup="vgRegister"></asp:TextBox>
                                <asp:RequiredFieldValidator ID="rfvPassword" runat="server" ErrorMessage="*" 
                                    ControlToValidate="txtPassword" Display="Dynamic" 
                                    ValidationGroup="vgRegister" />
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
                                <asp:Label ID="lblCPassword" runat="server" Text="確認密碼："></asp:Label>
                            </td>
                            <td align="left">
                                <asp:TextBox ID="txtCPassword" MaxLength="16" runat="server" 
                                    TextMode="Password" Width="160px" ValidationGroup="vgRegister"></asp:TextBox>
                                <asp:RequiredFieldValidator ID="rfvCPassword" runat="server" ErrorMessage="*" 
                                    ControlToValidate="txtCPassword" Display="Dynamic" 
                                    ValidationGroup="vgRegister" />
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
                                <asp:Label ID="lblEmail" runat="server" Text="E-Mail："></asp:Label>
                            </td>
                            <td align="left">
                                <asp:TextBox ID="txtEmail" MaxLength="50" runat="server" Width="160px" 
                                    ValidationGroup="vgRegister"></asp:TextBox>
                                <asp:RequiredFieldValidator ID="rfvEmail" runat="server" ErrorMessage="*" 
                                    ControlToValidate="txtEmail" Display="Dynamic" 
                                    ValidationGroup="vgRegister" />
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">&nbsp;</td>
                        </tr>
                        <tr>
                            <td colspan="2" align="center"><font color="blue">密碼忘記時使用(設定後不可變更)</font></td>
                        </tr>
                        <tr>
                            <td align="right">
                                <asp:Label ID="lblQuestion" runat="server" Text="提示問題："></asp:Label>
                            </td>
                            <td align="left">
                                <asp:TextBox ID="txtQuestion" MaxLength="100" runat="server" Width="160px" 
                                    ValidationGroup="vgRegister"></asp:TextBox>
                                <asp:RequiredFieldValidator ID="rfvQuestion" runat="server" ErrorMessage="*" 
                                    ControlToValidate="txtQuestion" Display="Dynamic" 
                                    ValidationGroup="vgRegister" />
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
                                <asp:Label ID="lblAnswer" runat="server" Text="答案："></asp:Label>
                            </td>
                            <td align="left">
                                <asp:TextBox ID="txtAnswer" MaxLength="100" runat="server" Width="160px" 
                                    ValidationGroup="vgRegister"></asp:TextBox>
                                <asp:RequiredFieldValidator ID="rfvAnswer" runat="server" ErrorMessage="*" 
                                    ControlToValidate="txtAnswer" Display="Dynamic" 
                                    ValidationGroup="vgRegister" />
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">&nbsp;</td>
                        </tr>
                        <tr>
                            <td align="right">
                                請輸入驗證碼：
                            </td>
                            <td align="left">
                                <asp:TextBox ID="txtCheckCode" runat="server" Width="160px" 
                                    ValidationGroup="vgRegister" EnableViewState="False"></asp:TextBox>
                                <asp:RequiredFieldValidator ID="RequiredFieldValidator8" runat="server" ControlToValidate="txtCheckCode"
                                    ErrorMessage="*" ValidationGroup="vgRegister"></asp:RequiredFieldValidator>
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td align="left">
                                <asp:UpdatePanel ID="UpdatePanel1" runat="server">
                                    <ContentTemplate>
                                        <l1j:tbvalidatecode ID="TBValidateCode1" runat="server" />
                                    </ContentTemplate>
                                </asp:UpdatePanel>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" align="center">
                                <asp:LinkButton ID="lbtnCreateUser" runat="server" ValidationGroup="vgRegister">建立帳號</asp:LinkButton>
                                &nbsp;&nbsp;
                                <asp:LinkButton ID="lbtnCancel" runat="server" onclientclick="return Clear()">清除</asp:LinkButton>
                            </td>
                        </tr>
                    </table>
                </asp:Panel>
            </td>
        </tr>
        <tr>
            <td align="center">
                <asp:CompareValidator ID="cvPassword" runat="server" 
                    ErrorMessage="密碼和確認密碼必須相符。<br />" ControlToCompare="txtPassword" 
                    ControlToValidate="txtCPassword" Display="Dynamic" 
                    ValidationGroup="vgRegister"></asp:CompareValidator>
                <asp:RegularExpressionValidator ID="revEmail" runat="server" 
                    ErrorMessage="E-Mail格式不正確。<br />" ControlToValidate="txtEmail" Display="Dynamic" 
                    ValidationExpression="\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*" 
                    ValidationGroup="vgRegister"></asp:RegularExpressionValidator>
                <asp:Label ID="lblMsg" runat="server" Visible="False"></asp:Label>
            </td>
        </tr>
    </table>
</asp:Panel>
