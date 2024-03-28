async function resetPassword({ data, t }) {
  console.log(data, t);
  const res = await fetch(
    `${import.meta.env.VITE_BACKEND}/api/v1/auth/reset-password`,
    {
      method: "POST",
      mode: "cors",
      cache: "no-cache",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
        "User-Agent": "react-front",
      },
      body: JSON.stringify({
        resetToken: data.token,
        newPassword: data.newPassword,
      }),
    }
  );
  if (!res.ok) {
    const err = await res.json();
    switch (err.title) {
      case "INVALID_TOKEN_ERROR":
        throw new Error(t("services.resetPasswordResetLinkInvalid"));
      case "PASSWORD_RESET_ERROR":
        throw new Error(t("services.resetPasswordOldPasswordError"));
      default:
        throw new Error(t("services.resetPasswordError"));
    }
  }
}

export default resetPassword;
