<!DOCTYPE html>
<head>
<link href="css/bootstrap.min.css" rel="stylesheet">
<script src="https://kit.fontawesome.com/c26f51318e.js"></script>
</head>

<c:choose>
	<c:when test="${ currentStep==1 }">
		<c:set var="classStep1" value="bg-primary" />
		<c:set var="classStep2" value="bg-light" />
		<c:set var="classStep3" value="bg-light" />
		<c:set var="classStep4" value="bg-light" />
	</c:when>
	<c:when test="${ currentStep==2 }">
		<c:set var="classStep1" value="bg-success" />
		<c:set var="classStep2" value="bg-primary" />
		<c:set var="classStep3" value="bg-light" />
		<c:set var="classStep4" value="bg-light" />
	</c:when>
	<c:when test="${ currentStep==3 }">
		<c:set var="classStep1" value="bg-success" />
		<c:set var="classStep2" value="bg-success" />
		<c:set var="classStep3" value="bg-primary" />
		<c:set var="classStep4" value="bg-light" />
	</c:when>
	<c:when test="${ currentStep==4 }">
		<c:set var="classStep1" value="bg-success" />
		<c:set var="classStep2" value="bg-success" />
		<c:set var="classStep3" value="bg-success" />
		<c:set var="classStep4" value="bg-primary" />
	</c:when>
</c:choose>

<body>
	<div class="container">
		<div class="card m-2">
			<h5 class="card-header ${ classStep1 }">1. Upload .SRT file</h5>
			<div class="card-body">
				<c:if test="${ Step1Error!=null }">
					<div class="alert alert-danger">
						<c:out value="${ Step1Error }" />
					</div>
				</c:if>
				<div class="alert alert-warning">Uploading a new file will
					overwrite your existing data.</div>
				<form method="post" action="srtTranslator"
					enctype="multipart/form-data">
					<div class="input-group mb-3">
						<div class="custom-file">
							<input type="file" class="custom-file-input" id="srtFile"
								name="srtFile"> <label class="custom-file-label"
								for="srtFile" aria-describedby="inputGroupFileAddon02">Choose
								a SRT file :</label>
						</div>
						<input type="hidden" name="actionStep" value="1">
						<div class="input-group-append">
							<input type="submit" class="btn input-group-text"
								id="inputGroupFileAddon02" value="Upload" />
						</div>
					</div>
				</form>
			</div>
		</div>
		<div class="card m-2">
			<h5 class="card-header ${ classStep2 }">2. Translate</h5>
			<div class="card-body">
				<c:if test="${ Step2Error!=null }">
					<div class="alert alert-danger">
						<c:out value="${ Step2Error }" />
					</div>
				</c:if>
				<c:if test="${ !empty srtFile }">
					<form method="post" action="srtTranslator">
						<table class="table table-sm" id="translateTable">
							<thead>
								<tr>
									<th scope="col" style="width: 10%">#</th>
									<th scope="col" style="width: 15%">Start</th>
									<th scope="col" style="width: 15%">End</th>
									<th scope="col" style="width: 30%">Text</th>
									<th scope="col" style="width: 30%">Translation</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${ srtFile }" var="srtItem"
									varStatus="itemStatus">
									<tr>
										<th scope="row"><c:out value="${ srtItem.rowId }" /></th>
										<td><c:out value="${ srtItem.start }" /></td>
										<td><c:out value="${ srtItem.end }" /></td>
										<td><c:out value="${ srtItem.text }" /></td>
										<td><input type="text"
											name="translation${itemStatus.index}"
											value="${ srtItem.translation }" class="form-control" /></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<input type="hidden" name="actionStep" value="2">
						<button type="submit" class="btn btn-primary">Save</button>
					</form>
				</c:if>
			</div>
		</div>
		<div class="card m-2">
			<h5 class="card-header ${ classStep3 }">3. Export result file</h5>
			<div class="card-body">
				<div class="container">
					<div class="row justify-content-center">
						<div class="col-4">
							<form method="post" action="srtTranslator">
								<input type="hidden" name="actionStep" value="3">
								<button class="btn btn-primary" type="submit">
									<i class="fas fa-download"></i> Download translated file
								</button>
							</form>
						</div>
						<div class="col-4">
							<form method="post" action="srtTranslator">
								<input type="hidden" name="actionStep" value="4">
								<button class="btn btn-secondary" type="submit">
									<i class="fas fa-download"></i> Download original file
								</button>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="card m-2">
			<h5 class="card-header ${ classStep4 }">4. Clear database</h5>
			<div class="card-body">
				<c:if test="${ Step4Error!=null }">
					<div class="alert alert-danger">
						<c:out value="${ Step4Error }" />
					</div>
				</c:if>
				<div class="container">
					<div class="row justify-content-center">
						<div class="col-4">
							<form method="post" action="srtTranslator">
								<input type="hidden" name="actionStep" value="5">
								<button class="btn btn-warning" type="submit">
									<i class="far fa-trash-alt"></i> Clear Database
								</button>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>

<script src="js/jquery-3.4.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery.dataTables.min.js"></script>
<script src="js/dataTables.bootstrap4.min.js"></script>

<!-- To activate pagination on datatable -->
<script type="text/javascript">
	$(document).ready(function() {
		$('#translateTable').DataTable();
	});
</script>

<!-- To display the filename in the upload form -->
<script>
	$('#srtFile').on('change', function() {
		//get the file name
		var fileName = $(this).val();
		//replace the "Choose a file" label
		$(this).next('.custom-file-label').html(fileName);
	})
</script>