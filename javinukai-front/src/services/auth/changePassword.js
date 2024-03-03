async function changePassword({ userData, formData, t }) {
  console.log("user data -> ", userData);
  console.log("form data -> ", formData);

  const res = await fetch(
    // `${import.meta.env.VITE_BACKEND}/api/v1/reset-password`,
    "http://localhost:8080/api/v1/auth/change-password",
    {
      method: "PATCH",
      mode: "cors",
      cache: "no-cache",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
        "User-Agent": "react-front",
      },
      // body: JSON.stringify(data),
      body: JSON.stringify({
        // resetToken: data.token,
        newPassword: formData.newPassword,
      }),
    }
  );
  if (!res.ok) {
    const err = await res.json();
    switch (err.title) {
      case "INVALID_TOKEN_ERROR":
        throw new Error(t("services.changePasswordResetLinkInvalid"));
      case "PASSWORD_RESET_ERROR":
        throw new Error(t("services.resetPasswordOldPasswordError"));
      default:
        throw new Error(t("services.changePasswordError"));
    }
  }
}
export default changePassword;
