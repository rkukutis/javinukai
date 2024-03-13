async function changePassword({ formData, t }) {
  console.log(`${import.meta.env.VITE_BACKEND}/api/v1/change-password`);
  const res = await fetch(
    `${import.meta.env.VITE_BACKEND}/api/v1/auth/change-password`,
    {
      method: "PATCH",
      mode: "cors",
      cache: "no-cache",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
        "User-Agent": "react-front",
      },
      body: JSON.stringify({
        newPassword: formData.newPassword,
        oldPassword: formData.oldPassword,
      }),
    }
  );

  if (!res.ok) {
    switch (res.status) {
      case 409:
        throw new Error(
          t("ChangeUsersPasswordForm.changePasswordFailWrongCurrent")
        );
      case 406:
        throw new Error(
          t("ChangeUsersPasswordForm.changePasswordFailAsCurrent")
        );

      default:
        new Error(t("services.changePasswordError"));
    }
  }
}
export default changePassword;
