export default async function () {
  const res = await fetch(
    `${import.meta.env.VITE_BACKEND}/api/v1/auth/logout`,
    {
      method: "POST",
      mode: "cors",
      cache: "no-cache",
      credentials: "include",
      headers: {
        "User-Agent": "react-front",
      },
    }
  );
  if (!res.ok) throw new Error();
}
