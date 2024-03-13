export default async function ({ contestId, t }) {
  const res = await fetch(
    `${import.meta.env.VITE_BACKEND}/api/v1/request?contestId=${contestId}`,
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
  return await res.json();
}
