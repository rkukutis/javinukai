async function changePassword({ formData, t }) {

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
      body: JSON.stringify({
        newPassword: formData.newPassword,
        oldPassword: formData.oldPassword,
      }),
    }
  );
  if (!res.ok) {
    throw new Error(t("services.changePasswordError"));
  }
}
export default changePassword;