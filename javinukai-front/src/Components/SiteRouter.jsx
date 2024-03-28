import { BrowserRouter, Route, Routes } from "react-router-dom";
import Layout from "./Layout";
import ContestsPage from "../pages/ContestsPage";
import ContestDetailsPage from "../pages/ContestDetailsPage";
import CreateContestPage from "../pages/CreateContestPage";
import ImageUpload from "./ImageUpload";
import UserSubmissionView from "./UserSubmissionView";
import JurySubmissionView from "./JurySubmissionView";
import LoginPage from "../pages/LoginPage";
import RegisterPage from "../pages/RegisterPage";
import CreateUserPage from "../pages/CreateUserPage";
import ForgotPassPage from "../pages/ForgotPassPage";
import ResetPassPage from "../pages/ResetPassPage";
import ConfirmEmailPage from "../pages/ConfirmEmailPage";
import PersonalInformation from "./user-management/PersonalInformation";
import UserManagementPage from "../pages/UserManagementPage";
import UserDetailsPage from "../pages/UserDetailsPage";
import ParticipationRequests from "./participation-request-components/ParticipationRequests";
import NotFoundPage from "../pages/NotFoundPage";
import LandingPage from "../pages/LandingPage";
import CreateContest from "./Contest-Components/CreateContest";
import ArchivePage from "../pages/ArchivePage";

function SiteRouter({ user }) {
  const userRole = user?.role;

  const getRoute = (currentRole, roles, path) => {
    if (roles.includes(currentRole)) {
      return path;
    }
  };

  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Layout />}>
            <Route index element={<LandingPage />} />

            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/forgot-password" element={<ForgotPassPage />} />
            <Route path="/reset-password" element={<ResetPassPage />} />
            <Route path="/confirm-email" element={<ConfirmEmailPage />} />

            <Route
              path={getRoute(
                userRole,
                ["ADMIN", "MODERATOR", "JURY", "USER", undefined],
                "/contests"
              )}
              element={<ContestsPage />}
            />

            <Route path="/contest/:contestId" element={<ContestDetailsPage />}>
              <Route
                path={getRoute(
                  userRole,
                  ["ADMIN", "MODERATOR", "JURY", "USER"],
                  "category/:categoryId/upload"
                )}
                element={<ImageUpload />}
              />
              <Route
                path={getRoute(
                  userRole,
                  ["ADMIN", "MODERATOR", "JURY", "USER"],
                  "category/:categoryId/my-entries"
                )}
                element={<UserSubmissionView />}
              />
            </Route>

            <Route
              path={getRoute(
                userRole,
                ["JURY", "ADMIN", "MODERATOR"],
                "/contest/:contestId/category/:categoryId/contestant-entries"
              )}
              element={<JurySubmissionView />}
            />

            <Route
              path={getRoute(
                userRole,
                ["ADMIN", "MODERATOR", "USER"],
                "/personal-info"
              )}
              element={<PersonalInformation />}
            />

            <Route
              path={getRoute(userRole, ["ADMIN"], "/manage-users")}
              element={<UserManagementPage />}
            />
            <Route
              path={getRoute(userRole, ["ADMIN"], "/manage-users/:userId")}
              element={<UserDetailsPage />}
            />

            <Route
              path={getRoute(userRole, ["ADMIN"], "/create-user")}
              element={<CreateUserPage />}
            />

            <Route
              path={getRoute(userRole, ["ADMIN"], "/contest-page")}
              element={<CreateContest />}
            />
            <Route
              path={getRoute(userRole, ["ADMIN"], "/contests/new")}
              element={<CreateContestPage />}
            />

            <Route
              path={getRoute(userRole, ["ADMIN", "MODERATOR"], "/requests")}
              element={<ParticipationRequests />}
            />

            <Route
              path={getRoute(userRole, ["ADMIN", "MODERATOR"], "/archive")}
              element={<ArchivePage />}
            />

            <Route path="/*" element={<NotFoundPage />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default SiteRouter;
