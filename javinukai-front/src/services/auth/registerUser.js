export default async function ({ registrationInfo, t }) {
  const res = await fetch(
    `${import.meta.env.VITE_BACKEND}/api/v1/auth/register`,
    {
      method: "POST",
      mode: "cors",
      cache: "no-cache",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
        "User-Agent": "react-front",
      },
      body: JSON.stringify(registrationInfo),
    }
  );
  if (!res.ok) {
    const err = await res.json();
    switch (err.title) {
      case "USER_ALREADY_EXISTS_ERROR":
        throw new Error(t("services.registerUserExistsError"));
      default:
        throw new Error(t("services.registerUserError"));
    }
  }
}
