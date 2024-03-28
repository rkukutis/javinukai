export default async function (contestId) {
  const res = await fetch(
    `${import.meta.env.VITE_BACKEND}/api/v1/contests/${contestId}/info`,
    {
      method: "GET",
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
  return await res.json();
}
