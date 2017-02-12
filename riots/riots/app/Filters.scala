import javax.inject.Inject
import play.api.http.HttpFilters
import play.filters.csrf.CSRFFilter
import play.filters.gzip.GzipFilter

class Filters @Inject() (csrfFilter: CSRFFilter, gzip: GzipFilter) extends HttpFilters {
  def filters = Seq(csrfFilter)
}
