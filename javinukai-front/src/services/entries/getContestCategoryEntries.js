export default async function (contestId, categoryId, page, limit, display) {
  const res = await fetch(
    `${
      import.meta.env.VITE_BACKEND
    }/api/v1/images/contest/${contestId}/category/${categoryId}?page=${page}&limit=${limit}&display=${display}`,
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
