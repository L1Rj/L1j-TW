<%@ Control Language="VB" AutoEventWireup="false" CodeFile="ForgotPwd.ascx.vb" Inherits="UserControl_Login_ForgotPwd" %>

<table border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td style="width:100%">
            <asp:Panel ID="pnlForgotPwd" runat="server">
                <table border="0" cellpadding="0" cellspacing="5">
                    <tr>
                        <td align="center" colspan="2">
                            忘記密碼?</td>
                    </tr>
                    <tr>
                        <td align="center" colspan="2">
                            請輸入您的帳號，以接收密碼。</td>
                    </tr>
                    
                    <tr>
                        <td align="right">
                            <asp:Label ID="lblAccount" runat="server">帳號:</asp:Label>
                        </td>
                        <td>
                            <asp:TextBox ID="txtAccount" runat="server"></asp:TextBox>
                            <asp:RequiredFieldValidator ID="AccountRequired" runat="server" 
                                ControlToValidate="txtAccount" ErrorMessage="必須提供帳號。" ToolTip="必須提供帳號。" 
                                ValidationGroup="PasswordRecovery1">*</asp:RequiredFieldValidator>
                        </td>
                    </tr>
                    <tr>
                        <td align="center" colspan="2" style="color:Red;">
                            <asp:Literal ID="FailureText" runat="server" EnableViewState="False"></asp:Literal>
                        </td>
                    </tr>
                    <tr>
                        <td align="right" colspan="2">
                            <asp:Button ID="SubmitButton" runat="server" BackColor="White" 
                                BorderColor="#507CD1" BorderStyle="Solid" BorderWidth="1px" CommandName="Submit" 
                                Font-Names="Verdana" ForeColor="#284E98" Text="確定" 
                                ValidationGroup="PasswordRecovery1" />
                        </td>
                    </tr>
                </table>
            </asp:Panel>
        </td>
    </tr>
    <tr>
        <td>
            <asp:Panel ID="pnlQandA" runat="server" Visible="false">
                <table border="0" cellpadding="0" cellspacing="5">
                    <tr>
                        <td align="right">
                            <asp:Label ID="lblQuestionT" runat="server" Text="問題："></asp:Label>
                        </td>
                        <td align="left">
                            <asp:Label ID="lblQuestion" runat="server"></asp:Label>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">
                            <asp:Label ID="lblAnswerT" runat="server" Text="答案："></asp:Label>
                        </td>
                        <td>
                            <asp:TextBox ID="txtAnswer" runat="server" MaxLength="100" Columns="50"></asp:TextBox>
                            <asp:RequiredFieldValidator ID="AnswerRequired" runat="server" 
                                ControlToValidate="txtAnswer" ErrorMessage="必須提供答案。" ToolTip="必須提供答案。" 
                                ValidationGroup="QandA">*</asp:RequiredFieldValidator>
                        </td>
                    </tr>
                    <tr>
                        <td align="center" colspan="2" style="color:Red;">
                            <asp:Literal ID="lblFailure" runat="server" EnableViewState="False"></asp:Literal>
                        </td>
                    </tr>
                    <tr>
                        <td align="right" colspan="2">
                            <asp:Button ID="bntSendEMail" runat="server" BackColor="White" 
                                BorderColor="#507CD1" BorderStyle="Solid" BorderWidth="1px" 
                                Font-Names="Verdana" ForeColor="#284E98" Text="確定" 
                                ValidationGroup="QandA" />
                        </td>
                    </tr>
                </table>
            </asp:Panel>
        </td>
    </tr>
</table>
