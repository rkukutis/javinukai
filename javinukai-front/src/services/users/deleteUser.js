export default async function (userId) {
  const res = await fetch(
    `${import.meta.env.VITE_BACKEND}/api/v1/users/${userId}`,
    {
      method: "DELETE",
      mode: "cors",
      cache: "no-cache",
      credentials: "include",
      headers: {
        "User-Agent": "react-front",
      },
    }
  );
  if (!res.ok) {
    const err = await res.json();
    throw new Error(err.title);
  }
}
