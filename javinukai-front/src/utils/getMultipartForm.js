export function getPhotoUploadMultipart(
  title,
  description,
  contestId,
  categoryId,
  images
) {
  let formData = new FormData();
  formData.append("categoryId", categoryId);
  formData.append("contestId", contestId);
  formData.append("title", title);
  formData.append("description", description);
  images.map((image) => formData.append("image", image));
  return formData;
}

export function getContestCreationMultipart(data, thumbnail) {
  let formData = new FormData();
  formData.append("data", JSON.stringify(data));
  formData.append("thumbnail", thumbnail);
  return formData;
}
