<%@ Page Language="VB" MasterPageFile="~/MasterPage.master" AutoEventWireup="false" CodeFile="ServerStatus.aspx.vb" Inherits="Server_ServerStatus" title="未命名頁面" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" Runat="Server">
    <ucServerStatus:ServerStatus ID="ServerStatus1" runat="server" />
</asp:Content>

