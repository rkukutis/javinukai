export default async function (registrationInfo) {
  const res = await fetch("http://localhost:8080/api/v1/auth/register", {
    method: "POST",
    mode: "cors",
    cache: "no-cache",
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
      "User-Agent": "react-front",
    },
    body: JSON.stringify(registrationInfo),
  });
  if (!res.ok) {
    const err = await res.json();
    switch (err.title) {
      case "USER_ALREADY_EXISTS_ERROR":
        throw new Error("An user with this email already exists");
      default:
        throw new Error("Registration failed. Please try again later");
    }
  }
}
